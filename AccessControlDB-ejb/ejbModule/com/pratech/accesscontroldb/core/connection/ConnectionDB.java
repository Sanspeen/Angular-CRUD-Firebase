package com.pratech.accesscontroldb.core.connection;

import com.pratech.accesscontroldb.DTO.LogSql;
import com.pratech.accesscontroldb.client.ACDBException;
import com.pratech.accesscontroldb.common.ACConfig;
import com.pratech.accesscontroldb.persistence.Store;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * Clase que realiza la conexión con la base de datos
 * 
 * @since 2011-07-01
 * @author Diego Piedrahita Ramirez
 * @email dpiedrahita@pratechgroup.com
 * @Company PRATECH S.A.S.
 * 
 */
public class ConnectionDB {

	/**
	 * Crea un objeto de conexion utlizado para las conexion a la base de datos
	 * que contiene la lista de instancias.
	 * 
	 * @param dataInstance Datos de conexión.
	 * @param errorDetail Si se recibe este argumento y ocurre 
	 *        un error de conexión, se almacenará en él detalles del error.
	 * @return
	 */
	public static Connection createConnection(Map<String, String> dataInstance) throws ACDBException {
		Connection con = null;
		LogSql logSql = new LogSql();
		String JDBC;
		String user;
		String pass;

		if (dataInstance.get("url") == null) {
			dataInstance.put("url", "");
		}
		if (dataInstance.get("url").length() > 0) {
			JDBC = dataInstance.get("url");
			user = dataInstance.get("user");
			pass = dataInstance.get("password");
		} else {
			JDBC = ACConfig.getValue("URL");
			user = ACConfig.getValue("user");
			pass = ACConfig.getValue("pass");
		}

		try {
			if (JDBC.toUpperCase().indexOf("ORACLE") >= 0) {
				Class.forName(ACConfig.getValue("JDBC.DRIVER.ORACLE"));
			} else {
				Class.forName(ACConfig.getValue("JDBC.DRIVER.SQLSERVER"));
			}
			con = DriverManager.getConnection(JDBC, user, pass);

		} catch (ClassNotFoundException e) {
			
			logSql.setUsuario("");
			logSql.setDescripcionAudit(e.getLocalizedMessage());
			logSql.setCod("AC4");
			logSql.setThrowable(e.getCause());
			Store.getInstance().save("4", logSql);
			
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error de clase no encontrada al tratar de abrir la conexión", e);
			
			throw new ACDBException("Error de conexi\u00f3n: [" +
						"tipo=" + e.getClass().getName() + ", " +
					"mensaje=" + e.getLocalizedMessage() + "]");
		} catch (SQLException e) {
			
			logSql.setUsuario("");
			logSql.setDescripcionAudit(e.getLocalizedMessage());
			logSql.setCod("AC4");
			logSql.setThrowable(e.getCause());
			Store.getInstance().save("4", logSql);
			
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error SQL al abrir la conexión", e);
			
			throw new ACDBException("Error de conexi\u00f3n: [" +
						"tipo=" + e.getClass().getName() + ", " +
						"mensaje=" + e.getLocalizedMessage() + ", " +
						"ErrorCode=" + e.getErrorCode() + ", " +
					"SQLState=" + e.getSQLState() + "]");
		} catch (Exception e) {
			
			logSql.setUsuario("");
			logSql.setDescripcionAudit(e.getLocalizedMessage());
			logSql.setCod("AC4");
			logSql.setThrowable(e.getCause());
			Store.getInstance().save("4", logSql);
			
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error general al abrir la conexión", e);
			
			throw new ACDBException("Error de conexi\u00f3n: [" +
						"tipo=" + e.getClass().getName() + ", " +
					"mensaje=" + e.getLocalizedMessage() + "]");
		}
		return con;
	}
}
