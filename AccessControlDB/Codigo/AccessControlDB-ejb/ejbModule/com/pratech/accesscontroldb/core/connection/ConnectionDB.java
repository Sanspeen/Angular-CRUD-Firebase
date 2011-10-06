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

	private final static Store store = null;

	/**
	 * Crea un objeto de conexion utlizado para las conexion a la base de datos
	 * que contiene la lista de instancias
	 * 
	 * @return
	 */
	public static Connection createConnection() {

		Connection con = null;
		LogSql logSql = new LogSql();

		try {
			Class.forName(ACConfig.getValue("JDBC.DRIVER"));
			String JDBC = ACConfig.getValue("URL");
			String user = ACConfig.getValue("user");
			String pass = ACConfig.getValue("pass");
			con = DriverManager.getConnection(JDBC, user, pass);
		} catch (ClassNotFoundException e) {
			logSql.setUsuario("");
			logSql.setDescripcionAudit(e.getLocalizedMessage());
			logSql.setCod("AC4");
			logSql.setThrowable(e.getCause());
			store.save("4", logSql);
		} catch (SQLException e) {
			logSql.setUsuario("");
			logSql.setDescripcionAudit(e.getLocalizedMessage());
			logSql.setCod("AC4");
			logSql.setThrowable(e.getCause());
			store.save("4", logSql);
		}
		return con;
	}

	/**
	 * Crear un objeto de conexión de acuerdo a los datos introducidos en la
	 * ventana de conexión, la conexión se utiliza para realizar consultas SQL
	 * 
	 * @return
	 */
	public static Connection lastConnection(Map<String, String> dataInstance) {
		Connection connection = null;
		LogSql logSql = new LogSql();

		try {
			Class.forName(ACConfig.getValue("JDBC.DRIVER"));
			String JDBC = dataInstance.get("url");
			String user = dataInstance.get("user");
			String pass = dataInstance.get("password");
			connection = DriverManager.getConnection(JDBC, user, pass);
		} catch (ClassNotFoundException e) {
			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setDescripcionAudit(e.getLocalizedMessage());
			logSql.setCod("AC4");
			logSql.setThrowable(e.getCause());
			store.save("4", logSql);
		} catch (SQLException e) {
			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setDescripcionAudit(e.getLocalizedMessage());
			logSql.setCod("AC4");
			logSql.setThrowable(e.getCause());
			store.save("4", logSql);
		}
		return connection;
	}
}
