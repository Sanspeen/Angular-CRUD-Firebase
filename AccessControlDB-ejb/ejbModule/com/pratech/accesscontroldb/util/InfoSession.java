package com.pratech.accesscontroldb.util;

import com.pratech.accesscontroldb.common.ACConfig;
import com.pratech.accesscontroldb.core.connection.ConnectionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase con utilidades para obtener los datos requeridos para establecer
 * sessión en el sistema.
 * 
 * @since 2011-07-01
 * @author Diego Piedrahita
 * @email dpiedrahita@pratechgroup.com
 * @Company PRATECH S.A.S.
 * 
 */
public class InfoSession {
	private Map<String, String> dataInstance = new HashMap<String, String>();

	/**
	 * Lista de ambientes de acceso a las bases de datos
	 * 
	 * @return List String
	 */
	public List<String> getAmbiente_() {
		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		List<String> listAmbiente = null;

		try {
			con = ConnectionDB.createConnection(dataInstance);
			statement = con.createStatement();
			boolean moreResults = statement.execute(ACConfig
					.getValue("SQLAmbiente"));
			listAmbiente = new ArrayList<String>();
			if (moreResults) {
				rs = statement.getResultSet();
				while (rs.next()) {
					listAmbiente.add(ACConfig.getValue(rs
							.getString("CDAMBIENTE")));
				}
			}

		} catch (SQLException ex) {
			Logger.getLogger("Error_Ambientes").log(Level.SEVERE, null, ex);
		} finally {
			try {
				statement.close();
			} catch (Exception ex) {
			}
			statement = null;
			try {
				rs.close();
			} catch (Exception ex) {
			}
			rs = null;
			try {
				con.close();
			} catch (Exception ex) {
			}
			con = null;
		}
		return listAmbiente;
	}

	/**
	 * Lista de instancias habilitadas en un ambiente
	 * 
	 * @param ambiente
	 * @return List
	 */
	public List<String> getInstancias_(String ambiente) {
		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		List<String> listInstancias = null;

		try {
			if (ambiente.length() > 0) {
				ambiente = ACConfig.getValue(ambiente);
				con = ConnectionDB.createConnection(dataInstance);
				statement = con.createStatement();
				String StrSQL = ACConfig.getValue("SQLIntancia1")
						+ ambiente.trim() + ACConfig.getValue("SQLIntancia2");
				boolean moreResults = statement.execute(StrSQL);

				listInstancias = new ArrayList<String>();
				if (moreResults) {
					rs = statement.getResultSet();
					while (rs.next()) {
						listInstancias.add(rs.getString("DSINSTANCIA"));
					}
				}
			}

		} catch (SQLException ex) {
			Logger.getLogger("Error_Instancias").log(Level.SEVERE, null, ex);
		} finally {
			try {
				statement.close();
			} catch (Exception ex) {
			}
			statement = null;
			try {
				rs.close();
			} catch (Exception ex) {
			}
			rs = null;
			try {
				con.close();
			} catch (Exception ex) {
			}
			con = null;
		}
		return listInstancias;
	}
	
	/**
	 * Exporta las instancias como XML de la tabla que las contiene.
	 * 
	 * @param ambiente
	 * @return List
	 */
	public String[] getXMLInstances() {
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		List<String> ambientes;
		int cdAmbiente;
		StringBuffer xmlInstancias = new StringBuffer();
		String[] result = new String[2];

		try {
			ambientes = getAmbiente_();
			con = ConnectionDB.createConnection(dataInstance);
			String StrSQLXML = ACConfig.getValue("SQLXMLIntanciasPorAmbiente");
			statement = con.prepareStatement(StrSQLXML);
			
			for (String ambiente : ambientes)
			{
				cdAmbiente = Integer.parseInt(ACConfig.getValue(ambiente));
				statement.setInt(1, cdAmbiente);
				statement.setInt(2, cdAmbiente);
				boolean moreResults = statement.execute();
				if (moreResults)
				{
					rs = statement.getResultSet();
					rs.next();
					xmlInstancias.append(rs.getString(1));
					try {
						rs.close();
					} catch (Exception ex) {
					}
					rs = null;
				}
				statement.clearParameters();
			}

		} catch (SQLException ex) {
			Logger.getLogger("Error_Instancias").log(Level.SEVERE, null, ex);
		} finally {
			try {
				rs.close();
			} catch (Exception ex) {
			}
			rs = null;
			try {
				statement.close();
			} catch (Exception ex) {
			}
			statement = null;
			try {
				con.close();
			} catch (Exception ex) {
			}
			con = null;
		}
		
		String xmlAmbientes = "<environments>" + xmlInstancias.toString() + "</environments>";
		result[0] = xmlAmbientes;
		result[1] = "&Eacute;xito actualizando archivo de instancias.";
		//TODO: recargar de ser necesario archivo.
		return result;
	}
}
