package com.pratech.accesscontroldb.core.ad;

import com.pratech.accesscontroldb.DTO.LogSql;
import com.pratech.accesscontroldb.DTO.ResponseDTO;
import com.pratech.accesscontroldb.client.ACDBException;
import com.pratech.accesscontroldb.core.connection.ConnectionDB;
import com.pratech.accesscontroldb.persistence.Store;
import com.pratech.accesscontroldb.util.SqlInstruction;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Utilidades para gestión de procesos PL/SQL
 * 
 * @since 2011-07-01
 * @author Diego Alberto Piedrahita
 * @email dpiedrahita@pratechgroup.com
 * @Company PRATECH S.A.S.
 * 
 */
public class JdbcUtil {

	/**
	 * Activar el búfer de conexión
	 * 
	 * @param conn
	 *            conection
	 * @param buffer_size
	 *            Size Buffer
	 */
	public static void enable_dbms_output(Connection conn, int buffer_size,
			Map<String, String> dataInstance) {
		try {
			CallableStatement stmt = conn
					.prepareCall("{call sys.dbms_output.enable(?) }");
			stmt.setInt(1, buffer_size);
			stmt.execute();
		} catch (Exception e) {
			LogSql logSql = new LogSql();
			logSql.setTransaccion(dataInstance.get("transaction"));
			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), dataInstance.get("scope") });
			logSql.setDescripcionAudit("Problem occurred while trying to enable dbms_output! "
					+ e.toString());
			logSql.setProceso("Class " + JdbcUtil.class.getName()
					+ ", Procedure enable_dbms_output");
			Store.getInstance().save("2", logSql);
			
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error general al habilitar salida del motor de la base de datos", e);
		}
	}

	/**
	 * Recoge la información que se muestra en el buffer
	 * 
	 * @param conn
	 * @param responseDTO
	 * @return
	 */
	public static ResponseDTO print_dbms_output(Connection conn,
			ResponseDTO responseDTO, Map<String, String> dataInstance) {
		try {
			CallableStatement stmt = conn
					.prepareCall("{call sys.dbms_output.get_line(?,?)}");
			stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
			stmt.registerOutParameter(2, java.sql.Types.NUMERIC);
			int status = 0;
			do {
				stmt.execute();
				if (stmt.getString(1) != null) {
					if (responseDTO.getSqlBuffer().length() > 0) {
						responseDTO.setSqlBuffer(responseDTO.getSqlBuffer()
								+ "\n\r" + stmt.getString(1));
					} else {
						responseDTO.setSqlBuffer(stmt.getString(1));
					}
				}
				status = stmt.getInt(2);
			} while (status == 0);
		} catch (Exception e) {
			String result = "Problem occurred during dump of dbms_output! "
					+ e.toString();
			LogSql logSql = new LogSql();
			logSql.setTransaccion(dataInstance.get("transaction"));
			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), dataInstance.get("scope") });
			logSql.setDescripcionAudit(result);
			logSql.setProceso("Class " + JdbcUtil.class.getName()
					+ ", Procedure print_dbms_output");
			Store.getInstance().save("2", logSql);
			responseDTO.setSqlBuffer(result);
			
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error al recuperar informacion mostrada en buffer", e);
		}
		return responseDTO;
	}

	public String[] getPrimaryKeys(Map<String, String> dataInstance,
			String StringSQL) throws ACDBException {
		Connection con = null;
		ResultSet rs = null;
		String[] primaryKeys = null;

		try {
			String nameTable = SqlInstruction.getTableFromSql(StringSQL);

			con = ConnectionDB.createConnection(dataInstance);
			DatabaseMetaData meta = con.getMetaData();
			rs = meta.getPrimaryKeys(null, null, nameTable);
			String keys = "";
			while (rs.next()) {
				keys += rs.getString("COLUMN_NAME") + ",";
			}			
			primaryKeys = keys.split(",");

		} catch (SQLException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error general al obtener la llave primaria", e);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				//Registrar excepción
				e.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error general al cerrar conjunto de resultados", e);
			}
			rs = null;
			try {
				con.close();
			} catch (Exception e) {
				//Registrar excepción
				e.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error general al cerrar la conexión", e);
			}
			con = null;
		}
		return primaryKeys;
	}
}
