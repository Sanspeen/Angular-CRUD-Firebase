
package co.com.suramericana.seus.ws.bo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import co.com.suramericana.seus.ws.servicios.ArrayOfString;


/**
 * <p>Java class for UsuarioSeus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UsuarioSeus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="omittedFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="perfiles" type="{http://servicios.ws.seus.suramericana.com.co/}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="dni" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delegationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="physicalLocationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="login" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="location" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ldapPath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="officeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UsuarioSeus", propOrder = {
    "omittedFlag",
    "perfiles",
    "dni",
    "delegationCode",
    "type",
    "physicalLocationCode",
    "login",
    "name",
    "location",
    "ldapPath",
    "officeCode",
    "mail"
})
public class UsuarioSeus {

    protected Boolean omittedFlag;
    @XmlElementRef(name = "perfiles", namespace = "http://bo.ws.seus.suramericana.com.co", type = JAXBElement.class)
    protected JAXBElement<ArrayOfString> perfiles;
    @XmlElementRef(name = "dni", namespace = "http://bo.ws.seus.suramericana.com.co", type = JAXBElement.class)
    protected JAXBElement<String> dni;
    @XmlElementRef(name = "delegationCode", namespace = "http://bo.ws.seus.suramericana.com.co", type = JAXBElement.class)
    protected JAXBElement<String> delegationCode;
    @XmlElementRef(name = "type", namespace = "http://bo.ws.seus.suramericana.com.co", type = JAXBElement.class)
    protected JAXBElement<String> type;
    @XmlElementRef(name = "physicalLocationCode", namespace = "http://bo.ws.seus.suramericana.com.co", type = JAXBElement.class)
    protected JAXBElement<String> physicalLocationCode;
    @XmlElementRef(name = "login", namespace = "http://bo.ws.seus.suramericana.com.co", type = JAXBElement.class)
    protected JAXBElement<String> login;
    @XmlElementRef(name = "name", namespace = "http://bo.ws.seus.suramericana.com.co", type = JAXBElement.class)
    protected JAXBElement<String> name;
    @XmlElementRef(name = "location", namespace = "http://bo.ws.seus.suramericana.com.co", type = JAXBElement.class)
    protected JAXBElement<String> location;
    @XmlElementRef(name = "ldapPath", namespace = "http://bo.ws.seus.suramericana.com.co", type = JAXBElement.class)
    protected JAXBElement<String> ldapPath;
    @XmlElementRef(name = "officeCode", namespace = "http://bo.ws.seus.suramericana.com.co", type = JAXBElement.class)
    protected JAXBElement<String> officeCode;
    @XmlElementRef(name = "mail", namespace = "http://bo.ws.seus.suramericana.com.co", type = JAXBElement.class)
    protected JAXBElement<String> mail;

    /**
     * Gets the value of the omittedFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOmittedFlag() {
        return omittedFlag;
    }

    /**
     * Sets the value of the omittedFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOmittedFlag(Boolean value) {
        this.omittedFlag = value;
    }

    /**
     * Gets the value of the perfiles property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfString }{@code >}
     *     
     */
    public JAXBElement<ArrayOfString> getPerfiles() {
        return perfiles;
    }

    /**
     * Sets the value of the perfiles property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfString }{@code >}
     *     
     */
    public void setPerfiles(JAXBElement<ArrayOfString> value) {
        this.perfiles = ((JAXBElement<ArrayOfString> ) value);
    }

    /**
     * Gets the value of the dni property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDni() {
        return dni;
    }

    /**
     * Sets the value of the dni property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDni(JAXBElement<String> value) {
        this.dni = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the delegationCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDelegationCode() {
        return delegationCode;
    }

    /**
     * Sets the value of the delegationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDelegationCode(JAXBElement<String> value) {
        this.delegationCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setType(JAXBElement<String> value) {
        this.type = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the physicalLocationCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPhysicalLocationCode() {
        return physicalLocationCode;
    }

    /**
     * Sets the value of the physicalLocationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPhysicalLocationCode(JAXBElement<String> value) {
        this.physicalLocationCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the login property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLogin() {
        return login;
    }

    /**
     * Sets the value of the login property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLogin(JAXBElement<String> value) {
        this.login = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setName(JAXBElement<String> value) {
        this.name = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLocation(JAXBElement<String> value) {
        this.location = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the ldapPath property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLdapPath() {
        return ldapPath;
    }

    /**
     * Sets the value of the ldapPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLdapPath(JAXBElement<String> value) {
        this.ldapPath = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the officeCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOfficeCode() {
        return officeCode;
    }

    /**
     * Sets the value of the officeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOfficeCode(JAXBElement<String> value) {
        this.officeCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mail property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMail() {
        return mail;
    }

    /**
     * Sets the value of the mail property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMail(JAXBElement<String> value) {
        this.mail = ((JAXBElement<String> ) value);
    }

}
