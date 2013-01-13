//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.26 at 01:04:13 AM EET 
//


package org.xmlsoap.schemas.devprof;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

import org.w3._2001.xmlschema.Adapter1;
import org.w3c.dom.Element;
import org.xmlsoap.schemas.addressing.EndpointReferenceType;


/**
 * <p>Java class for HostServiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HostServiceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://schemas.xmlsoap.org/ws/2004/08/addressing}EndpointReference" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://schemas.xmlsoap.org/ws/2006/02/devprof}Types" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.xmlsoap.org/ws/2006/02/devprof}ServiceId"/>
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HostServiceType", propOrder = {
    "endpointReference",
    "types",
    "serviceId",
    "any"
})
public class HostServiceType {

    @XmlElement(name = "EndpointReference", namespace = "http://schemas.xmlsoap.org/ws/2004/08/addressing", required = true)
    protected List<EndpointReferenceType> endpointReference;
    @XmlList
    @XmlElement(name = "Types")
    protected List<QName> types;
    @XmlElement(name = "ServiceId", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "anyURI")
    protected URI serviceId;
    @XmlAnyElement(lax = true)
    protected List<Object> any;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the endpointReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the endpointReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEndpointReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EndpointReferenceType }
     * 
     * 
     */
    public List<EndpointReferenceType> getEndpointReference() {
        if (endpointReference == null) {
            endpointReference = new ArrayList<EndpointReferenceType>();
        }
        return this.endpointReference;
    }

    /**
     * Gets the value of the types property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the types property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTypes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QName }
     * 
     * 
     */
    public List<QName> getTypes() {
        if (types == null) {
            types = new ArrayList<QName>();
        }
        return this.types;
    }

    /**
     * Gets the value of the serviceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public URI getServiceId() {
        return serviceId;
    }

    /**
     * Sets the value of the serviceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceId(URI value) {
        this.serviceId = value;
    }

    /**
     * Gets the value of the any property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * {@link Element }
     * 
     * 
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

    
    // Helper Methods
    
//    @XmlElement(name = "EndpointReference", namespace = "http://schemas.xmlsoap.org/ws/2004/08/addressing", required = true)
//    protected List<EndpointReferenceType> endpointReference;

    public void addEndpointReference(EndpointReferenceType epr){
    	getEndpointReference().add(epr);
    }
    
    public void addEndpointReference(String url){
    	try {
			getEndpointReference().add(new EndpointReferenceType(url));
		} catch (URISyntaxException e) {
			System.err.println("Endpoint Reference not created.  Reason: ");
			e.printStackTrace();
		}
    }
    
    public void addType(QName type){
    	getTypes().add(type);
    }
    
    public void addAny(Object o){
    	getAny().add(o);
    }
    
    public void addOtherAttribute(QName name, String value){
    	getOtherAttributes().put(name, value);
    }
    
    public String getOtherAttribute(QName name){
    	return getOtherAttributes().get(name);
    }
    
    
}
