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
import org.apache.camel.impl.DefaultCamelContext;

/**
 *
 * @author Johannes
 */
public class ClientLauncher {

    DpwsClient client;
    DiscoveryManager discovery;
    String defaultEventSink;
    String eventTypeFilter = "";
    String networkInterface = "en0";
    String host = "192.168.2.135";
    int port = 46835;
    String eventSinkPath = "eventSink";
    CamelContext context;
    Map<String,String> namespaces;

    public String getEventSink() {
        
        if(defaultEventSink == null){
            return String.format("http://%s:%d/%s", host, port, eventSinkPath);
        }
        return defaultEventSink;
    }

    public static void main(String[] args) throws Exception {

        ClientLauncher launcher = new ClientLauncher();
        launcher.init();

        Thread t;

        ProgRunner testProg = new ProgRunner();
        t = new Thread(testProg);
        t.start();

        while (true) {
            int cag = System.in.read();
            System.out.println(cag);
            if (cag == 113) {
                break;
            }
        }

        testProg.kill();

        t.stop();
    }

    public void destroy() throws Exception {
        context.stop();
        client.destroy();
        discovery.destroy();
    }

    public void init() throws Exception {


        context = new DefaultCamelContext();

                
        client = new DpwsClient();
        client.setEventTypeFilter(eventTypeFilter);
        client.setDefaultEventSink(getEventSink());
        client.setCamelContext(context);


        discovery = new DiscoveryManager();
        discovery.setNetworkInterface(networkInterface);
        discovery.setHost(host);
        discovery.setCamelContext(context);


        // Camel Routes



        context.addRoutes(new RouteBuilder() {
            public void configure() {
                
                Map<String,String>  namespaces = new HashMap<String,String>();
                namespaces.put("wsa","http://schemas.xmlsoap.org/ws/2004/08/addressing");
                namespaces.put("s12","http://www.w3.org/2003/05/soap-envelope");
                namespaces.put("wsd","http://schemas.xmlsoap.org/ws/2005/04/discovery");
                namespaces.put("wsdp","http://schemas.xmlsoap.org/ws/2006/02/devprof");
                namespaces.put("env","http://www.w3.org/2003/05/soap-envelope");
        
                JaxbDataFormat soapWSD = new JaxbDataFormat("org.w3._2003._05.soap_envelope:" + 
                                                            "org.xmlsoap.schemas.discovery:" +
                                                            "org.xmlsoap.schemas.eventing:" +
                                                            "org.xmlsoap.schemas.addressing:" +
                                                            "org.xmlsoap.schemas.mex:" +
                                                            "org.xmlsoap.schemas.transfer:" +
                                                            "org.xmlsoap.schemas.devprof:" +
                                                            "org.xmlsoap.schemas.wsdl:" +
                                                            "org.xmlsoap.schemas.wsdl.soap12");

                // Multicast Listening
                from("multicast://239.255.255.250:3702?networkInterface=" + networkInterface)
                        .choice()
                        .when().xpath("/s12:Envelope/s12:Header/wsa:Action[text()='" + DPWSConstants.WSD_HELLO_ACTION + "']",namespaces)
                        .unmarshal(soapWSD)
                        .bean(client, "helloReceived")
                        .when().xpath("/s12:Envelope/s12:Header/wsa:Action[text()='" + DPWSConstants.WSD_BYE_ACTION + "']",namespaces)
                        .unmarshal(soapWSD)
                        .bean(client, "byeReceived")
                        .when().xpath("/s12:Envelope/s12:Header/wsa:Action[text()='" + DPWSConstants.WSD_PROBE_ACTION + "']",namespaces)
                        .to("log:fi.tut.fast.DpwsClient?level=INFO")
                        .otherwise()
                        .bean(client, "messageReceived");
                // Outgoing Probes
                from("direct:discoveryProbe")
                        .bean(discovery, "sendProbe");

                // Incoming Probe Matches
                from("direct:discoveryManager")
                        .choice()
                        .when().xpath("/s12:Envelope/s12:Header/wsa:Action[text()='" + DPWSConstants.WSD_PROBEMATCHES_ACTION + "']",namespaces)
                        .unmarshal(soapWSD)
                        .bean(client, "probeMatchesReceived");

                // Event Endpoint

                from(String.format("jetty:http://%s:%d/%s", host, port, eventSinkPath))
                        .setHeader("newAddress", ExpressionBuilder.constantExpression("http://someNewAddress:123/thing"))
                        .setHeader("newAction", ExpressionBuilder.constantExpression("http://namespace.org/Service/Port/Action"))
                        .to("xslt:file:///Users/Johannes/Desktop/eventToInput.xslt")
                        .bean(client, "eventReceived");

            }
        });

        context.start();
        discovery.init();
        client.init();



    }

    static class ProgRunner implements Runnable {

        static boolean kill = false;

        @Override
        public void run() {
            try {
                ClientLauncher launcher = new ClientLauncher();
                launcher.init();
                while (!kill) {
                    Thread.sleep(50);
                }
                launcher.destroy();
            } catch (Exception e) {
                System.err.println("Oops:");
                e.printStackTrace();
            }
        }

        public void kill() {
            kill = true;
        }
    }
}
