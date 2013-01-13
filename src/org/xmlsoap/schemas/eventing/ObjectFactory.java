//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.16 at 04:00:04 PM EET 
//


package org.xmlsoap.schemas.eventing;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.xmlsoap.schemas.addressing.EndpointReferenceType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.xmlsoap.schemas.eventing package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SupportedDialect_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/08/eventing", "SupportedDialect");
    private final static QName _NotifyTo_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/08/eventing", "NotifyTo");
    private final static QName _Identifier_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/08/eventing", "Identifier");
    private final static QName _SupportedDeliveryMode_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/08/eventing", "SupportedDeliveryMode");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.xmlsoap.schemas.eventing
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SubscribeResponse }
     * 
     */
    public SubscribeResponse createSubscribeResponse() {
        return new SubscribeResponse();
    }

    /**
     * Create an instance of {@link Renew }
     * 
     */
    public Renew createRenew() {
        return new Renew();
    }

    /**
     * Create an instance of {@link RenewResponse }
     * 
     */
    public RenewResponse createRenewResponse() {
        return new RenewResponse();
    }

    /**
     * Create an instance of {@link GetStatusResponse }
     * 
     */
    public GetStatusResponse createGetStatusResponse() {
        return new GetStatusResponse();
    }

    /**
     * Create an instance of {@link SubscriptionEnd }
     * 
     */
    public SubscriptionEnd createSubscriptionEnd() {
        return new SubscriptionEnd();
    }

    /**
     * Create an instance of {@link LanguageSpecificStringType }
     * 
     */
    public LanguageSpecificStringType createLanguageSpecificStringType() {
        return new LanguageSpecificStringType();
    }

    /**
     * Create an instance of {@link GetStatus }
     * 
     */
    public GetStatus createGetStatus() {
        return new GetStatus();
    }

    /**
     * Create an instance of {@link Unsubscribe }
     * 
     */
    public Unsubscribe createUnsubscribe() {
        return new Unsubscribe();
    }

    /**
     * Create an instance of {@link Subscribe }
     * 
     */
    public Subscribe createSubscribe() {
        return new Subscribe();
    }

    /**
     * Create an instance of {@link DeliveryType }
     * 
     */
    public DeliveryType createDeliveryType() {
        return new DeliveryType();
    }

    /**
     * Create an instance of {@link FilterType }
     * 
     */
    public FilterType createFilterType() {
        return new FilterType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.xmlsoap.org/ws/2004/08/eventing", name = "SupportedDialect")
    public JAXBElement<String> createSupportedDialect(String value) {
        return new JAXBElement<String>(_SupportedDialect_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EndpointReferenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.xmlsoap.org/ws/2004/08/eventing", name = "NotifyTo")
    public JAXBElement<EndpointReferenceType> createNotifyTo(EndpointReferenceType value) {
        return new JAXBElement<EndpointReferenceType>(_NotifyTo_QNAME, EndpointReferenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.xmlsoap.org/ws/2004/08/eventing", name = "Identifier")
    public JAXBElement<String> createIdentifier(String value) {
        return new JAXBElement<String>(_Identifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.xmlsoap.org/ws/2004/08/eventing", name = "SupportedDeliveryMode")
    public JAXBElement<String> createSupportedDeliveryMode(String value) {
        return new JAXBElement<String>(_SupportedDeliveryMode_QNAME, String.class, null, value);
    }

}
