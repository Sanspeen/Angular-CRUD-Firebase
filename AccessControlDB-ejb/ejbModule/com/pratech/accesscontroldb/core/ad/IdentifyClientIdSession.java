package com.pratech.accesscontroldb.core.ad;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Map;

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
			cstmt.setString(1, dataInstance.get("analyst").trim()
					+ ":AccessControlDB:" + dataInstance.get("ip").trim());
			cstmt.execute();
		} catch (Exception e) {
			System.out.println("Error conec " + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			cstmt = null;
		}
	}
}
