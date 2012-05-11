package com.pratech.accesscontroldb.server;

import suramericana.swb.cache.SuraCacheManager;
import suramericana.swb.security.SecurityAgent;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pratech.accesscontroldb.client.AdminService;
import com.pratech.accesscontroldb.core.SqlEngineSB;

public class AdminServiceImpl extends RemoteServiceServlet implements
AdminService {

	private static final long serialVersionUID = 1L;

	public String updateInstancesXML() {
		
		boolean updateInstancesAccess;
		boolean urlAdminServAccess;
		SecurityAgent securityAgent;
		
		try {
			securityAgent = SuraCacheManager.getSaFromCache(getThreadLocalRequest(), "ControlAccesoBd");
		} catch (Exception e) {
			e.printStackTrace();
			return "Ocurrió una excepción de tipo " + e.getClass().getName() + ". Mensaje: " + e.getMessage() + ".";
		}
		
		try {
			//String resources = securityAgent.getResources();
			//System.out.println("resources : " + resources);
			updateInstancesAccess = securityAgent.hasAccess("ActualizarInstancias");
			urlAdminServAccess = securityAgent.hasAccessToUrl("/accesscontroldb_war/adminservice");
			if (updateInstancesAccess & urlAdminServAccess)
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
			e.printStackTrace();
			return "Ocurrió una excepción de tipo " + e.getClass().getName() + " durante la actualización de las instancias. Mensaje: " + e.getMessage() + ".";
		}
		
	}

}
