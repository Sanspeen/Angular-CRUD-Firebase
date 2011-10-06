
package co.com.suramericana.seus.ws.bo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import co.com.suramericana.seus.ws.servicios.ArrayOfString;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the co.com.suramericana.seus.ws.bo package. 
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

    private final static QName _AutenticacionSeusTokenPubs_QNAME = new QName("http://bo.ws.seus.suramericana.com.co", "tokenPubs");
    private final static QName _AutenticacionSeusNombre_QNAME = new QName("http://bo.ws.seus.suramericana.com.co", "nombre");
    private final static QName _AutenticacionSeusTokenMus_QNAME = new QName("http://bo.ws.seus.suramericana.com.co", "tokenMus");
    private final static QName _AplicacionSeusCategoria_QNAME = new QName("http://bo.ws.seus.suramericana.com.co", "categoria");
    private final static QName _AplicacionSeusUrl_QNAME = new QName("http://bo.ws.seus.suramericana.com.co", "url");
    private final static QName _AplicacionSeusDescripcion_QNAME = new QName("http://bo.ws.seus.suramericana.com.co", "descripcion");
    private final static QName _AplicacionSeusObservaciones_QNAME = new QName("http://bo.ws.seus.suramericana.com.co", "observaciones");
    private final static QName _UsuarioSeusDelegationCode_QNAME = new QName("http://bo.ws.seus.suramericana.com.co", "delegationCode");
    private final static QName _UsuarioSeusOfficeCode_QNAME = new QName("http://bo.ws.seus.suramericana.com.co", "officeCode");
    private final static QName _UsuarioSeusLogin_QNAME = new QName("http://bo.ws.seus.suramericana.com.co", "login");
    private final static QName _UsuarioSeusLocation_QNAME = new QName("http://bo.ws.seus.suramericana.com.co", "location");
    private final static QName _UsuarioSeusPhysicalLocationCode_QNAME = new QName("http://bo.ws.seus.suramericana.com.co", "physicalLocationCode");
    private final static QName _UsuarioSeusPerfiles_QNAME = new QName("http://bo.ws.seus.suramericana.com.co", "perfiles");
    private final static QName _UsuarioSeusMail_QNAME = new QName("http://bo.ws.seus.suramericana.com.co", "mail");
    private final static QName _UsuarioSeusLdapPath_QNAME = new QName("http://bo.ws.seus.suramericana.com.co", "ldapPath");
    private final static QName _UsuarioSeusName_QNAME = new QName("http://bo.ws.seus.suramericana.com.co", "name");
    private final static QName _UsuarioSeusDni_QNAME = new QName("http://bo.ws.seus.suramericana.com.co", "dni");
    private final static QName _UsuarioSeusType_QNAME = new QName("http://bo.ws.seus.suramericana.com.co", "type");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: co.com.suramericana.seus.ws.bo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AutenticacionSeus }
     * 
     */
    public AutenticacionSeus createAutenticacionSeus() {
        return new AutenticacionSeus();
    }

    /**
     * Create an instance of {@link AplicacionSeus }
     * 
     */
    public AplicacionSeus createAplicacionSeus() {
        return new AplicacionSeus();
    }

    /**
     * Create an instance of {@link ArrayOfAplicacionSeus }
     * 
     */
    public ArrayOfAplicacionSeus createArrayOfAplicacionSeus() {
        return new ArrayOfAplicacionSeus();
    }

    /**
     * Create an instance of {@link UsuarioSeus }
     * 
     */
    public UsuarioSeus createUsuarioSeus() {
        return new UsuarioSeus();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "tokenPubs", scope = AutenticacionSeus.class)
    public JAXBElement<String> createAutenticacionSeusTokenPubs(String value) {
        return new JAXBElement<String>(_AutenticacionSeusTokenPubs_QNAME, String.class, AutenticacionSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "nombre", scope = AutenticacionSeus.class)
    public JAXBElement<String> createAutenticacionSeusNombre(String value) {
        return new JAXBElement<String>(_AutenticacionSeusNombre_QNAME, String.class, AutenticacionSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "tokenMus", scope = AutenticacionSeus.class)
    public JAXBElement<String> createAutenticacionSeusTokenMus(String value) {
        return new JAXBElement<String>(_AutenticacionSeusTokenMus_QNAME, String.class, AutenticacionSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "categoria", scope = AplicacionSeus.class)
    public JAXBElement<String> createAplicacionSeusCategoria(String value) {
        return new JAXBElement<String>(_AplicacionSeusCategoria_QNAME, String.class, AplicacionSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "nombre", scope = AplicacionSeus.class)
    public JAXBElement<String> createAplicacionSeusNombre(String value) {
        return new JAXBElement<String>(_AutenticacionSeusNombre_QNAME, String.class, AplicacionSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "url", scope = AplicacionSeus.class)
    public JAXBElement<String> createAplicacionSeusUrl(String value) {
        return new JAXBElement<String>(_AplicacionSeusUrl_QNAME, String.class, AplicacionSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "descripcion", scope = AplicacionSeus.class)
    public JAXBElement<String> createAplicacionSeusDescripcion(String value) {
        return new JAXBElement<String>(_AplicacionSeusDescripcion_QNAME, String.class, AplicacionSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "observaciones", scope = AplicacionSeus.class)
    public JAXBElement<String> createAplicacionSeusObservaciones(String value) {
        return new JAXBElement<String>(_AplicacionSeusObservaciones_QNAME, String.class, AplicacionSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "delegationCode", scope = UsuarioSeus.class)
    public JAXBElement<String> createUsuarioSeusDelegationCode(String value) {
        return new JAXBElement<String>(_UsuarioSeusDelegationCode_QNAME, String.class, UsuarioSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "officeCode", scope = UsuarioSeus.class)
    public JAXBElement<String> createUsuarioSeusOfficeCode(String value) {
        return new JAXBElement<String>(_UsuarioSeusOfficeCode_QNAME, String.class, UsuarioSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "login", scope = UsuarioSeus.class)
    public JAXBElement<String> createUsuarioSeusLogin(String value) {
        return new JAXBElement<String>(_UsuarioSeusLogin_QNAME, String.class, UsuarioSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "location", scope = UsuarioSeus.class)
    public JAXBElement<String> createUsuarioSeusLocation(String value) {
        return new JAXBElement<String>(_UsuarioSeusLocation_QNAME, String.class, UsuarioSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "physicalLocationCode", scope = UsuarioSeus.class)
    public JAXBElement<String> createUsuarioSeusPhysicalLocationCode(String value) {
        return new JAXBElement<String>(_UsuarioSeusPhysicalLocationCode_QNAME, String.class, UsuarioSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "perfiles", scope = UsuarioSeus.class)
    public JAXBElement<ArrayOfString> createUsuarioSeusPerfiles(ArrayOfString value) {
        return new JAXBElement<ArrayOfString>(_UsuarioSeusPerfiles_QNAME, ArrayOfString.class, UsuarioSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "mail", scope = UsuarioSeus.class)
    public JAXBElement<String> createUsuarioSeusMail(String value) {
        return new JAXBElement<String>(_UsuarioSeusMail_QNAME, String.class, UsuarioSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "ldapPath", scope = UsuarioSeus.class)
    public JAXBElement<String> createUsuarioSeusLdapPath(String value) {
        return new JAXBElement<String>(_UsuarioSeusLdapPath_QNAME, String.class, UsuarioSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "name", scope = UsuarioSeus.class)
    public JAXBElement<String> createUsuarioSeusName(String value) {
        return new JAXBElement<String>(_UsuarioSeusName_QNAME, String.class, UsuarioSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "dni", scope = UsuarioSeus.class)
    public JAXBElement<String> createUsuarioSeusDni(String value) {
        return new JAXBElement<String>(_UsuarioSeusDni_QNAME, String.class, UsuarioSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bo.ws.seus.suramericana.com.co", name = "type", scope = UsuarioSeus.class)
    public JAXBElement<String> createUsuarioSeusType(String value) {
        return new JAXBElement<String>(_UsuarioSeusType_QNAME, String.class, UsuarioSeus.class, value);
    }

}
