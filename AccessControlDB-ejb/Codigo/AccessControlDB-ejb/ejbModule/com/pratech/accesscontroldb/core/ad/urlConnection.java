package com.pratech.accesscontroldb.core.ad;

import com.pratech.accesscontroldb.DTO.LogSql;
import com.pratech.accesscontroldb.common.ACConfig;
import com.pratech.accesscontroldb.core.connection.ConnectionDB;
import com.pratech.accesscontroldb.persistence.Store;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Obtiene la URL deacuerdo
 * 
 * @since 2011-07-01
 * @author Diego Alberto Piedrahita
 * @email dpiedrahita@pratechgroup.com
 * @Company PRATECH S.A.S.
 * @author 20110701 dpiedrahita - dpiedrahita@pratechgroup.com
 */
public class urlConnection {

	/**
	 * Busca la URL en base de datos segun los datos ingresados en la ventana
	 * conexion
	 * 
	 * @param dataInstance = datos de la instancia que se desea conectar.
	 * @return  URL
	 */
	public String URLSearch(Map<String, String> dataInstance) {
		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		Store store = new Store();
		String url = "";
		LogSql logSql = new LogSql();

		try {
			con = ConnectionDB.createConnection();
			statement = con.createStatement();
			String Sql = "select DSURL from TADD_INSTANCIA where CDAMBIENTE = "
					+ ACConfig.getValue(dataInstance.get("scope").trim())
					+ " and DSINSTANCIA ='"
					+ dataInstance.get("instance").trim() + "' order by DSURL";
			boolean moreResults = statement.execute(Sql);

			if (moreResults) {
				rs = statement.getResultSet();
				if (rs.next()) {
					url = rs.getString("DSURL");
				}
			}
		} catch (SQLException ex) {
			logSql.setUsuario(dataInstance.get("user"));
			logSql.setDescripcionAudit(ex.getLocalizedMessage());
			logSql.setCamposTexto(new String[] { dataInstance.get("user"),
					dataInstance.get("analyst") });
			store.save("2", logSql);
		} catch (Exception e) {
			logSql.setUsuario(dataInstance.get("user"));
			logSql.setDescripcionAudit(e.getLocalizedMessage());
			logSql.setCamposTexto(new String[] { dataInstance.get("user"),
					dataInstance.get("analyst") });
			store.save("2", logSql);
		} finally {
			try {
				statement.close();
			} catch (Exception ex) {
				logSql.setUsuario(dataInstance.get("user"));
				logSql.setDescripcionAudit(ex.getLocalizedMessage());
				logSql.setCamposTexto(new String[] { dataInstance.get("user"),
						dataInstance.get("analyst") });
				store.save("2", logSql);
			}
			statement = null;
			try {
				con.close();
			} catch (Exception ex) {
				logSql.setUsuario(dataInstance.get("user"));
				logSql.setDescripcionAudit(ex.getLocalizedMessage());
				logSql.setCamposTexto(new String[] { dataInstance.get("user"),
						dataInstance.get("analyst") });
				store.save("2", logSql);
			}
			con = null;
		}
		return url;
	}
}
