package com.pratech.accesscontroldb.core.ad;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Map;

import com.pratech.accesscontroldb.persistence.Store;

public class IdentifyClientIdSession {

	/**
	 * Marca el Client identifier en la sesión.
	 * 
	 * @param connection
	 */
	public static void identifyClientIdSession(Connection con,
			Map<String, String> dataInstance) {
		CallableStatement cstmt = null;
		try {
			String SP = "{call dbms_session.set_identifier(?)}";
			cstmt = con.prepareCall(SP);
			cstmt.setString(1, (dataInstance.get("analyst") != null? dataInstance.get("analyst").trim() : "")
					+ ":AccessControlDB:" + dataInstance.get("ip").trim());
			cstmt.execute();
		} catch (Exception e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error general al identificar sesion", e);
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
				}
			} catch (Exception e) {
				//Registrar excepción
				e.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error general al cerrar sentencia", e);
			}
			cstmt = null;
		}
	}
}
