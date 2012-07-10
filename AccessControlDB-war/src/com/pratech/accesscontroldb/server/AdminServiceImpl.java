package com.pratech.accesscontroldb.server;

import suramericana.swb.cache.SuraCacheManager;
import suramericana.swb.security.SecurityAgent;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pratech.accesscontroldb.client.AdminService;
import com.pratech.accesscontroldb.core.SqlEngineSB;
import com.pratech.accesscontroldb.persistence.Store;

public class AdminServiceImpl extends RemoteServiceServlet implements
AdminService {

	private static final long serialVersionUID = 1L;

	public String updateInstancesXML() {
		
		boolean updateInstancesAccess;
		SecurityAgent securityAgent;
		
		try {
			securityAgent = SuraCacheManager.getSaFromCache(getThreadLocalRequest(), "ControlAccesoBd");
		} catch (Exception e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error al obtener security agent", e);
			
			return "Ocurrió una excepción de tipo " + e.getClass().getName() + ". Mensaje: " + e.getMessage() + ".";
		}
		
		try {
			//String resources = securityAgent.getResources();
			//System.out.println("resources : " + resources);
			updateInstancesAccess = securityAgent.hasAccess("ActualizarInstancias");
			if (updateInstancesAccess)
			{
				SqlEngineSB sqlEngineSB = new SqlEngineSB();
				String result = sqlEngineSB.updateInstancesXML();
				return result;
			}
			else
			{
				return "No está autorizado para ejecutar la funcionalidad.";
			}
		} catch (Exception e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error ocurrió durante la actualización de las instancias", e);

			return "Ocurrió una excepción de tipo " + e.getClass().getName() + " durante la actualización de las instancias. Mensaje: " + e.getMessage() + ".";
		}
		
	}

}
