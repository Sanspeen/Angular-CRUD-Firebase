package com.pratech.accesscontroldb.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Interface methods for asynchronous calls to the server
 * 
 * @since 2012-03-30
 * @author Juan Esteban Jaramillo
 * @email jjaramillo@pratech.com.co
 * @Company PRATECH S.A.S.
 * 
 */
@RemoteServiceRelativePath("adminservice")
public interface AdminService extends RemoteService {

	/**
	 * Actualiza el archivo de instancias XML.
	 * 
	 * @return Mensaje con el resultado de la solicitud de actualización.
	 */
	public String updateInstancesXML();
	
}
