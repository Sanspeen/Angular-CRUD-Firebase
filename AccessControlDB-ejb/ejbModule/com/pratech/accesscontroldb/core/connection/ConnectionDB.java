package com.pratech.accesscontroldb.core.connection;

import com.pratech.accesscontroldb.DTO.LogSql;
import com.pratech.accesscontroldb.common.ACConfig;
import com.pratech.accesscontroldb.persistence.Store;

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

	private final static Store store = new Store();


	/**
	 * Crea un objeto de conexion utlizado para las conexion a la base de datos
	 * que contiene la lista de instancias.
	 * 
	 * @param dataInstance Datos de conexión.
	 * @param errorDetail Si se recibe este argumento y ocurre 
	 *        un error de conexión, se almacenará en él detalles del error.
	 * @return
	 */
	public static Connection createConnection(Map<String, String> dataInstance, StringBuffer errorDetail) {
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
			//Se solicitó poner en la salida estándar
			//los mensajes de error al crear conexión
			e.printStackTrace(System.out);
			
			logSql.setUsuario("");
			logSql.setDescripcionAudit(e.getLocalizedMessage());
			logSql.setCod("AC4");
			logSql.setThrowable(e.getCause());
			store.save("4", logSql);
			
			//Obtener string detallado
			if (errorDetail != null)
				errorDetail.append(
						"Error de conexi\u00f3n: [" +
						"tipo=" + e.getClass().getName() + ", " +
						"mensaje=" + e.getLocalizedMessage() + "]"
				);
		} catch (SQLException e) {
			//Se solicitó poner en la salida estándar
			//los mensajes de error al crear conexión
			e.printStackTrace(System.out);
			
			logSql.setUsuario("");
			logSql.setDescripcionAudit(e.getLocalizedMessage());
			logSql.setCod("AC4");
			logSql.setThrowable(e.getCause());
			store.save("4", logSql);
			
			//Obtener string detallado
			if (errorDetail != null)
				errorDetail.append(
						"Error de conexi\u00f3n: [" +
						"tipo=" + e.getClass().getName() + ", " +
						"mensaje=" + e.getLocalizedMessage() + ", " +
						"ErrorCode=" + e.getErrorCode() + ", " +
						"SQLState=" + e.getSQLState() + "]"
				);
		} catch (Exception e) {
			//Se solicitó poner en la salida estándar
			//los mensajes de error al crear conexión
			e.printStackTrace(System.out);
			
			logSql.setUsuario("");
			logSql.setDescripcionAudit(e.getLocalizedMessage());
			logSql.setCod("AC4");
			logSql.setThrowable(e.getCause());
			store.save("4", logSql);
			
			//Obtener string detallado
			if (errorDetail != null)
				errorDetail.append(
						"Error de conexi\u00f3n: [" +
						"tipo=" + e.getClass().getName() + ", " +
						"mensaje=" + e.getLocalizedMessage() + "]"
				);
		}
		return con;
	}
}
