//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.26 at 01:04:13 AM EET 
//


package org.xmlsoap.schemas.devprof;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DeviceRelationshipTypeURIs.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DeviceRelationshipTypeURIs">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyURI">
 *     &lt;enumeration value="http://schemas.xmlsoap.org/ws/2006/02/devprof/host"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DeviceRelationshipTypeURIs")
@XmlEnum
public enum DeviceRelationshipTypeURIs {

    @XmlEnumValue("http://schemas.xmlsoap.org/ws/2006/02/devprof/host")
    host("http://schemas.xmlsoap.org/ws/2006/02/devprof/host");
    private final String value;

    DeviceRelationshipTypeURIs(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DeviceRelationshipTypeURIs fromValue(String v) {
        for (DeviceRelationshipTypeURIs c: DeviceRelationshipTypeURIs.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
