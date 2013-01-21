/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.tut.fast.dpws;

import fi.tut.fast.dpws.discovery.DiscoveryManager;
import java.util.HashMap;
import java.util.Map;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.ExpressionBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.main.Main;

/**
 *
 * @author Johannes
 */
public class ClientLauncher {

    private Main main;
    DpwsClient client;
    DiscoveryManager discovery;
    TestBean testBean;
    String defaultEventSink;
    String eventTypeFilter = "";
    String networkInterface = "en0";
    String host = "192.168.2.135";
    int port = 46835;
    String eventSinkPath = "eventSink";
    CamelContext context;
    Map<String, String> namespaces;

    public String getEventSink() {

        if (defaultEventSink == null) {
            return String.format("http://%s:%d/%s", host, port, eventSinkPath);
        }
        return defaultEventSink;
    }

    public static void main(String[] args) throws Exception {
        ClientLauncher launcher = new ClientLauncher();
        launcher.init();
        launcher.boot();
    }

    public void boot() throws Exception {

        testBean = new TestBean();
        
        client = new DpwsClient();
        client.setEventTypeFilter(eventTypeFilter);
        client.setDefaultEventSink(getEventSink());
        client.setCamelContext(context);


        discovery = new DiscoveryManager();
        discovery.setNetworkInterface(networkInterface);
        discovery.setHost(host);
        discovery.setCamelContext(context);


        main = new Main();
        main.enableHangupSupport();
        main.bind("clientBean", client);
        main.bind("discoveryBean", discovery);
        main.addRouteBuilder(new RouteBuilder() {
            public void configure() {

                Map<String, String> namespaces = new HashMap<String, String>();
                namespaces.put("wsa", "http://schemas.xmlsoap.org/ws/2004/08/addressing");
                namespaces.put("s12", "http://www.w3.org/2003/05/soap-envelope");
                namespaces.put("wsd", "http://schemas.xmlsoap.org/ws/2005/04/discovery");
                namespaces.put("wsdp", "http://schemas.xmlsoap.org/ws/2006/02/devprof");
                namespaces.put("env", "http://www.w3.org/2003/05/soap-envelope");

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
                        .bean("clientBean", "helloReceived")
                        .when().xpath("/s12:Envelope/s12:Header/wsa:Action[text()='" + DPWSConstants.WSD_BYE_ACTION + "']", namespaces)
                        .unmarshal(soapWSD)
                        .bean("clientBean", "byeReceived")
                        .when().xpath("/s12:Envelope/s12:Header/wsa:Action[text()='" + DPWSConstants.WSD_PROBE_ACTION + "']", namespaces)
                        .to("log:fi.tut.fast.DpwsClient?level=INFO")
                        .otherwise()
                        .bean("clientBean", "messageReceived");
                // Outgoing Probes
                from("direct:discoveryProbe")
                        .bean("discoveryBean", "sendProbe");

                // Incoming Probe Matches
                from("direct:discoveryManager")
                        .choice()
                        .when().xpath("/s12:Envelope/s12:Header/wsa:Action[text()='" + DPWSConstants.WSD_PROBEMATCHES_ACTION + "']", namespaces)
                        .unmarshal(soapWSD)
                        .bean("clientBean", "probeMatchesReceived");

                // Event Endpoint

                from(String.format("jetty:http://%s:%d/%s", host, port, eventSinkPath))
                        .setHeader("newAddress", ExpressionBuilder.constantExpression("http://someNewAddress:123/thing"))
                        .setHeader("newAction", ExpressionBuilder.constantExpression("http://namespace.org/Service/Port/Action"))
                        .to("xslt:file:///Users/Johannes/Desktop/eventToInput.xslt")
                        .bean("clientBean", "eventReceived");


//                // TESAT
//                from(String.format("jetty:http://%s:%d/%s", host, 13579, "testInput"))
////                        .setExchangePattern(ExchangePattern.InOut)
//                        .bean(testBean, "stepOne")
//                        .multicast()
//                            .to("direct:fetchAdditionalData")
//                            .bean(testBean,"stepThree")
//                        .end()
//                        .pollEnrich("direct:delayedStepTwo",2000, new AggregationStrategy(){
//                                @Override
//                                public Exchange aggregate(Exchange ex1, Exchange ex2) {
//                                    String bOne = ex1.getIn().getBody(String.class);
//                                    String bTwo = ex2.getIn().getBody(String.class);
//
//                                    ex1.getIn().setBody(String.format("[ex1: %s -- ex2: %s]",bOne,bTwo));
//                                    return ex1;
//                                }
//                            });
//                
//                from("direct:fetchAdditionalData")
//                        .delay(1000)
//                        .bean(testBean, "stepTwo")
//                        .to("direct:delayedStepTwo");
//                
//                <route>
//                    <from uri="jetty:http://192.168.3.123:13579/whatever"/>
//                   <process ref="whateverMarshalBean"/>
//                   <multicast>
//                        <to uri="activemq:someOuttopic"/>
//                   </multicast>
//                    <pollEnrich uri="activemq:inputTopic" timeout="5000" strategyRef="someBeanThatImplementsAggregationStrategy"/>
//                <route>


            }
        });

        main.run();
    }

    public void destroy() throws Exception {
        client.destroy();
        discovery.destroy();
    }

    public void init() throws Exception {
    }
}
