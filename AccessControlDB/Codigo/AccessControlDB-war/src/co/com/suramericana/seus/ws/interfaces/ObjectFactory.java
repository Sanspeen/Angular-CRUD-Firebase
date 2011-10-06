
package co.com.suramericana.seus.ws.interfaces;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import co.com.suramericana.seus.ws.bo.ArrayOfAplicacionSeus;
import co.com.suramericana.seus.ws.bo.AutenticacionSeus;
import co.com.suramericana.seus.ws.bo.UsuarioSeus;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the co.com.suramericana.seus.ws.interfaces package. 
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

    private final static QName _GetInfoUsuarioFromTokenSeusToken_QNAME = new QName("", "token");
    private final static QName _AutenticarResponseReturn_QNAME = new QName("", "return");
    private final static QName _GetInfoUsuarioFromTokenSeus_QNAME = new QName("http://interfaces.ws.seus.suramericana.com.co/", "getInfoUsuarioFromTokenSeus");
    private final static QName _GetAplicacionesUsuario_QNAME = new QName("http://interfaces.ws.seus.suramericana.com.co/", "getAplicacionesUsuario");
    private final static QName _GetInfoUsuarioFromTokenSeusResponse_QNAME = new QName("http://interfaces.ws.seus.suramericana.com.co/", "getInfoUsuarioFromTokenSeusResponse");
    private final static QName _GetAplicacionesUsuarioResponse_QNAME = new QName("http://interfaces.ws.seus.suramericana.com.co/", "getAplicacionesUsuarioResponse");
    private final static QName _AutenticarResponse_QNAME = new QName("http://interfaces.ws.seus.suramericana.com.co/", "AutenticarResponse");
    private final static QName _Autenticar_QNAME = new QName("http://interfaces.ws.seus.suramericana.com.co/", "Autenticar");
    private final static QName _AutenticarPassword_QNAME = new QName("", "password");
    private final static QName _AutenticarLogin_QNAME = new QName("", "login");
    private final static QName _AutenticarIp_QNAME = new QName("", "ip");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: co.com.suramericana.seus.ws.interfaces
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetAplicacionesUsuario }
     * 
     */
    public GetAplicacionesUsuario createGetAplicacionesUsuario() {
        return new GetAplicacionesUsuario();
    }

    /**
     * Create an instance of {@link GetInfoUsuarioFromTokenSeus }
     * 
     */
    public GetInfoUsuarioFromTokenSeus createGetInfoUsuarioFromTokenSeus() {
        return new GetInfoUsuarioFromTokenSeus();
    }

    /**
     * Create an instance of {@link AutenticarResponse }
     * 
     */
    public AutenticarResponse createAutenticarResponse() {
        return new AutenticarResponse();
    }

    /**
     * Create an instance of {@link GetInfoUsuarioFromTokenSeusResponse }
     * 
     */
    public GetInfoUsuarioFromTokenSeusResponse createGetInfoUsuarioFromTokenSeusResponse() {
        return new GetInfoUsuarioFromTokenSeusResponse();
    }

    /**
     * Create an instance of {@link GetAplicacionesUsuarioResponse }
     * 
     */
    public GetAplicacionesUsuarioResponse createGetAplicacionesUsuarioResponse() {
        return new GetAplicacionesUsuarioResponse();
    }

    /**
     * Create an instance of {@link Autenticar }
     * 
     */
    public Autenticar createAutenticar() {
        return new Autenticar();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "token", scope = GetInfoUsuarioFromTokenSeus.class)
    public JAXBElement<String> createGetInfoUsuarioFromTokenSeusToken(String value) {
        return new JAXBElement<String>(_GetInfoUsuarioFromTokenSeusToken_QNAME, String.class, GetInfoUsuarioFromTokenSeus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "token", scope = GetAplicacionesUsuario.class)
    public JAXBElement<String> createGetAplicacionesUsuarioToken(String value) {
        return new JAXBElement<String>(_GetInfoUsuarioFromTokenSeusToken_QNAME, String.class, GetAplicacionesUsuario.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AutenticacionSeus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "return", scope = AutenticarResponse.class)
    public JAXBElement<AutenticacionSeus> createAutenticarResponseReturn(AutenticacionSeus value) {
        return new JAXBElement<AutenticacionSeus>(_AutenticarResponseReturn_QNAME, AutenticacionSeus.class, AutenticarResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UsuarioSeus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "return", scope = GetInfoUsuarioFromTokenSeusResponse.class)
    public JAXBElement<UsuarioSeus> createGetInfoUsuarioFromTokenSeusResponseReturn(UsuarioSeus value) {
        return new JAXBElement<UsuarioSeus>(_AutenticarResponseReturn_QNAME, UsuarioSeus.class, GetInfoUsuarioFromTokenSeusResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAplicacionSeus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "return", scope = GetAplicacionesUsuarioResponse.class)
    public JAXBElement<ArrayOfAplicacionSeus> createGetAplicacionesUsuarioResponseReturn(ArrayOfAplicacionSeus value) {
        return new JAXBElement<ArrayOfAplicacionSeus>(_AutenticarResponseReturn_QNAME, ArrayOfAplicacionSeus.class, GetAplicacionesUsuarioResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetInfoUsuarioFromTokenSeus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://interfaces.ws.seus.suramericana.com.co/", name = "getInfoUsuarioFromTokenSeus")
    public JAXBElement<GetInfoUsuarioFromTokenSeus> createGetInfoUsuarioFromTokenSeus(GetInfoUsuarioFromTokenSeus value) {
        return new JAXBElement<GetInfoUsuarioFromTokenSeus>(_GetInfoUsuarioFromTokenSeus_QNAME, GetInfoUsuarioFromTokenSeus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAplicacionesUsuario }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://interfaces.ws.seus.suramericana.com.co/", name = "getAplicacionesUsuario")
    public JAXBElement<GetAplicacionesUsuario> createGetAplicacionesUsuario(GetAplicacionesUsuario value) {
        return new JAXBElement<GetAplicacionesUsuario>(_GetAplicacionesUsuario_QNAME, GetAplicacionesUsuario.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetInfoUsuarioFromTokenSeusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://interfaces.ws.seus.suramericana.com.co/", name = "getInfoUsuarioFromTokenSeusResponse")
    public JAXBElement<GetInfoUsuarioFromTokenSeusResponse> createGetInfoUsuarioFromTokenSeusResponse(GetInfoUsuarioFromTokenSeusResponse value) {
        return new JAXBElement<GetInfoUsuarioFromTokenSeusResponse>(_GetInfoUsuarioFromTokenSeusResponse_QNAME, GetInfoUsuarioFromTokenSeusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAplicacionesUsuarioResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://interfaces.ws.seus.suramericana.com.co/", name = "getAplicacionesUsuarioResponse")
    public JAXBElement<GetAplicacionesUsuarioResponse> createGetAplicacionesUsuarioResponse(GetAplicacionesUsuarioResponse value) {
        return new JAXBElement<GetAplicacionesUsuarioResponse>(_GetAplicacionesUsuarioResponse_QNAME, GetAplicacionesUsuarioResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AutenticarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://interfaces.ws.seus.suramericana.com.co/", name = "AutenticarResponse")
    public JAXBElement<AutenticarResponse> createAutenticarResponse(AutenticarResponse value) {
        return new JAXBElement<AutenticarResponse>(_AutenticarResponse_QNAME, AutenticarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Autenticar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://interfaces.ws.seus.suramericana.com.co/", name = "Autenticar")
    public JAXBElement<Autenticar> createAutenticar(Autenticar value) {
        return new JAXBElement<Autenticar>(_Autenticar_QNAME, Autenticar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "password", scope = Autenticar.class)
    public JAXBElement<String> createAutenticarPassword(String value) {
        return new JAXBElement<String>(_AutenticarPassword_QNAME, String.class, Autenticar.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "login", scope = Autenticar.class)
    public JAXBElement<String> createAutenticarLogin(String value) {
        return new JAXBElement<String>(_AutenticarLogin_QNAME, String.class, Autenticar.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ip", scope = Autenticar.class)
    public JAXBElement<String> createAutenticarIp(String value) {
        return new JAXBElement<String>(_AutenticarIp_QNAME, String.class, Autenticar.class, value);
    }

}
