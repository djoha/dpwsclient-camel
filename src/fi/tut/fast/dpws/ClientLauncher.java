/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.tut.fast.dpws;

import fi.tut.fast.dpws.discovery.DiscoveryManager;
import fi.tut.fast.xml.NamespaceContextImpl;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.language.bean.BeanExpression;
import org.apache.camel.main.Main;
import org.xml.sax.InputSource;

/**
 *
 * @author Johannes
 */
public class ClientLauncher {

    private ClientApp main;
    DpwsClient client;
    DiscoveryManager discovery;
    TestBean testBean;
    String defaultEventSink;
    String eventTypeFilter = "";
    String networkInterface = "en0";
    String host = "192.168.2.135";
    int port = 46835;
    String eventSinkPath = "eventSink";
    Map<String, String> namespaces;
    ClientLauncher launcher;

    public static void main(String[] args) throws Exception {
        (new ClientLauncher()).boot();
    }

    public boolean useCaseSpecificEventFilter(Exchange ex) {

        List<String> expectedEvents = new ArrayList<String>();
        expectedEvents.add("http://www.tut.fi/wsdl/SomeService/SomeServicePortType/somethingHappened");
        expectedEvents.add("http://www.tut.fi/fast/wsdl/SomeServicePort/someOtherEventType");

        XPathFactory xpfactory = XPathFactory.newInstance();
        XPath xpath = xpfactory.newXPath();
        xpath.setNamespaceContext(new NamespaceContextImpl(namespaces));

        String actionPath = "/s12:Envelope/s12:Header/wsa:Action/text()";
        InputSource src = new InputSource(new StringReader(ex.getIn().getBody(String.class)));

        String result;
        try {
            XPathExpression expr = xpath.compile(actionPath);
            result = (String) expr.evaluate(src, XPathConstants.STRING);
        } catch (XPathException e) {
            System.out.println("Error evaluating XPath Expression " + actionPath);
            e.printStackTrace();
            return false;
        }

        return expectedEvents.contains(result);

    }

    public String getEventSink() {

        if (defaultEventSink == null) {
            return String.format("http://%s:%d/%s", host, port, eventSinkPath);
        }
        return defaultEventSink;
    }

    public void boot() throws Exception {

        launcher = this;

        testBean = new TestBean();

        discovery = new DiscoveryManager();
        discovery.setNetworkInterface(networkInterface);
        discovery.setHost(host);

        client = new DpwsClient();
        client.setEventTypeFilter(eventTypeFilter);
        client.setDefaultEventSink(getEventSink());
        client.setDiscoveryManager(discovery);

        namespaces = new HashMap<String, String>();
        namespaces.put("wsa", "http://schemas.xmlsoap.org/ws/2004/08/addressing");
        namespaces.put("s12", "http://www.w3.org/2003/05/soap-envelope");
        namespaces.put("wsd", "http://schemas.xmlsoap.org/ws/2005/04/discovery");
        namespaces.put("wsdp", "http://schemas.xmlsoap.org/ws/2006/02/devprof");
        namespaces.put("env", "http://www.w3.org/2003/05/soap-envelope");

        main = new ClientApp();
        main.enableHangupSupport();
        main.bind("clientBean", client);
        main.bind("discoveryBean", discovery);
        main.addRouteBuilder(new RouteBuilder() {
            public void configure() {

                // --- --- --- -- --- -- -- -- -- -- -- -- -- --- -- -- //
                //           Event Endpoint                             //
                // --- --- --- --- --- --- --- --- --- --- --- --- --- -//                   
                String eventId = "http://www.tut.fi/wsdl/SomeService/SomeServicePortType/somethingElseHappened";

                from(String.format("jetty:http://%s:%d/%s", host, port, eventSinkPath))
                        .filter()
                            .xpath("/s12:Envelope/s12:Header/wsa:Action[text()='" + eventId + "']", namespaces)
                            .to("xslt:fi/tut/fast/xml/extractSoapBody.xslt")
                            .multicast()
                                .to("jetty:http://localhost:8008/droolsTest?bridgeEndpoint=true")
                                .to("log:matchedEvent")
                            .end()
                        .end()
                        .filter(new BeanExpression(launcher, "useCaseSpecificEventFilter"))
                            .to("log:useCaseSpecificEventFilter.")
                        .end();


                // --- --- --- -- --- -- -- -- -- -- -- -- -- --- -- -- //
                //                 Other DPWS-Related Stuff.

                JaxbDataFormat soapWSD = new JaxbDataFormat("org.w3._2003._05.soap_envelope:"
                        + "org.xmlsoap.schemas.discovery:"
                        + "org.xmlsoap.schemas.eventing:"
                        + "org.xmlsoap.schemas.addressing:"
                        + "org.xmlsoap.schemas.mex:"
                        + "org.xmlsoap.schemas.transfer:"
                        + "org.xmlsoap.schemas.devprof:"
                        + "org.xmlsoap.schemas.wsdl:"
                        + "org.xmlsoap.schemas.wsdl.soap12");

                // Multicast Listening
                from("multicast://239.255.255.250:3702?networkInterface=" + networkInterface)
                        .choice()
                        .when().xpath("/s12:Envelope/s12:Header/wsa:Action[text()='" + DPWSConstants.WSD_HELLO_ACTION + "']", namespaces)
                        .unmarshal(soapWSD)
                        .bean(client, "helloReceived")
                        .when().xpath("/s12:Envelope/s12:Header/wsa:Action[text()='" + DPWSConstants.WSD_BYE_ACTION + "']", namespaces)
                        .unmarshal(soapWSD)
                        .bean(client, "byeReceived")
                        .when().xpath("/s12:Envelope/s12:Header/wsa:Action[text()='" + DPWSConstants.WSD_PROBE_ACTION + "']", namespaces)
                        .to("log:fi.tut.fast.DpwsClient?level=INFO")
                        .otherwise()
                        .bean(client, "messageReceived");
                // Outgoing Probes
                from("direct:discoveryProbe")
                        .bean(discovery, "sendProbe");

                // Incoming Probe Matches
                from("direct:discoveryManager")
                        .choice()
                        .when().xpath("/s12:Envelope/s12:Header/wsa:Action[text()='" + DPWSConstants.WSD_PROBEMATCHES_ACTION + "']", namespaces)
                        .unmarshal(soapWSD)
                        .bean(client, "probeMatchesReceived");


            }
        });

        main.run();
    }

    class ClientApp extends Main {

        @Override
        protected void beforeStop() {
            super.beforeStop();
            try {
                discovery.destroy();
                client.destroy();
            } catch (Exception ex) {
                System.err.println("Error caught during shutdown:");
                ex.printStackTrace();
            }
        }

        @Override
        protected void afterStart() {
            super.afterStart();
            try {
                System.out.println("Application Started....");
                discovery.setProducerTemplate(main.getCamelTemplate());
                discovery.init();
                client.init();
            } catch (Exception e) {
                System.out.println("Error starting Client:");
                e.printStackTrace();
            }
        }
    }
}
