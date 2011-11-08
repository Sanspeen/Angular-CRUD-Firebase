
package co.com.suramericana.seus.ws.interfaces;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import co.com.suramericana.seus.ws.bo.UsuarioSeus;


/**
 * <p>Java class for getInfoUsuarioFromTokenSeusResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getInfoUsuarioFromTokenSeusResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://bo.ws.seus.suramericana.com.co}UsuarioSeus" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getInfoUsuarioFromTokenSeusResponse", propOrder = {
    "_return"
})
public class GetInfoUsuarioFromTokenSeusResponse {

    @XmlElementRef(name = "return", type = JAXBElement.class)
    protected JAXBElement<UsuarioSeus> _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link UsuarioSeus }{@code >}
     *     
     */
    public JAXBElement<UsuarioSeus> getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link UsuarioSeus }{@code >}
     *     
     */
    public void setReturn(JAXBElement<UsuarioSeus> value) {
        this._return = ((JAXBElement<UsuarioSeus> ) value);
    }

}
