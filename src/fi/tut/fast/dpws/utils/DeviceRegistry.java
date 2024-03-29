package fi.tut.fast.dpws.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xmlbeans.XmlObject;
import org.xmlsoap.schemas.discovery.ByeType;
import org.xmlsoap.schemas.discovery.HelloType;
import org.xmlsoap.schemas.discovery.ProbeMatchType;
import org.xmlsoap.schemas.discovery.ProbeMatchesType;

import fi.tut.fast.dpws.device.remote.DeviceRef;
import fi.tut.fast.dpws.device.remote.OperationReference;

public class DeviceRegistry {


	private static final transient Logger logger = Logger
			.getLogger(DeviceRegistry.class.getName());
	
	Map<URI,DeviceRef> knownDevices;
	
	public DeviceRegistry(){
		knownDevices = new HashMap<URI,DeviceRef>();
	}
	
	// Adding DeviceRef
	public DeviceRef registerDevice(HelloType hello){
		URI id = hello.getEndpointReference().getAddress().getValue();
		DeviceRef ref =  DeviceRef.fromHello(hello);
		registerDeviceInternal(id, DeviceRef.fromHello(hello));
		return ref;
	}
	
	public List<DeviceRef> registerDevice(ProbeMatchesType matches){
		List<DeviceRef> deviceRefs = new ArrayList<DeviceRef>();
		for(ProbeMatchType pm :  matches.getProbeMatch()){
			URI id = pm.getEndpointReference().getAddress().getValue();
			DeviceRef ref = DeviceRef.fromProbeMatch(pm);
			deviceRefs.add(ref);
			registerDeviceInternal(id, ref);
		}
		return deviceRefs;
	}
	
	private void registerDeviceInternal(URI id, DeviceRef dev){
		
		if(knownDevices.containsKey(id)){
			logger.info("Ignoring device " + id.toString());
			return;
		}
		knownDevices.put(id, dev);

		logger.info("Device Discovered:" + dev.toString());
//
//		// Test Operation Invokation
//		
//		OperationReference op1 = dev.getService("SomeService").getOperation(
//				"OperationOne");
//
//		OperationReference ev1 = dev.getService("SomeService").getOperation(
//				"somethingElseHappened");
//		
//		
//		
//		Map<String, String> params = new HashMap<String, String>();
//
//		params.put("K1", "4.2");
//		params.put("K2", "6.3");
//		params.put("@lang", "FR");
//
//		XmlObject iobj = op1.getInputParamter(params);
//
//		System.out.println("Invoking OperationOne:");
//		try {
//			String refNum = ev1.subscribe("http://192.168.2.135:8808/esink");
//			DPWSXmlUtil.getInstance().writeXml(iobj);
//			XmlObject oobj = op1.invoke(iobj);
//			System.out.println("Response:");
//			DPWSXmlUtil.getInstance().writeXml(oobj);
//		} catch (Exception ex) {
//			logger.log(Level.SEVERE, "Error invoking OperationOne", ex);
//		}

	}
	
	public DeviceRef getDevice(URI id){
		return knownDevices.get(id);
	}
	
	public DeviceRef removeDevice(URI id){
		return knownDevices.remove(id);
	}
	
	public void reportBye(URI id){
		DeviceRef dev = knownDevices.get(id);
		if(dev != null){
			dev.reportBye();
		}
	}
	
	public void reportBye(ByeType bye){
		 reportBye(bye.getEndpointReference().getAddress().getValue());
		 logger.info("Bye Message Recieved from " + bye.getEndpointReference().getAddress().getValue());
	}
}
