
package co.com.suramericana.seus.ws.bo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AutenticacionSeus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AutenticacionSeus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tokenPubs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tokenMus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AutenticacionSeus", propOrder = {
    "tokenPubs",
    "tokenMus",
    "nombre"
})
public class AutenticacionSeus {

    @XmlElementRef(name = "tokenPubs", namespace = "http://bo.ws.seus.suramericana.com.co", type = JAXBElement.class)
    protected JAXBElement<String> tokenPubs;
    @XmlElementRef(name = "tokenMus", namespace = "http://bo.ws.seus.suramericana.com.co", type = JAXBElement.class)
    protected JAXBElement<String> tokenMus;
    @XmlElementRef(name = "nombre", namespace = "http://bo.ws.seus.suramericana.com.co", type = JAXBElement.class)
    protected JAXBElement<String> nombre;

    /**
     * Gets the value of the tokenPubs property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTokenPubs() {
        return tokenPubs;
    }

    /**
     * Sets the value of the tokenPubs property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTokenPubs(JAXBElement<String> value) {
        this.tokenPubs = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the tokenMus property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTokenMus() {
        return tokenMus;
    }

    /**
     * Sets the value of the tokenMus property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTokenMus(JAXBElement<String> value) {
        this.tokenMus = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the nombre property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNombre() {
        return nombre;
    }

    /**
     * Sets the value of the nombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNombre(JAXBElement<String> value) {
        this.nombre = ((JAXBElement<String> ) value);
    }

}
