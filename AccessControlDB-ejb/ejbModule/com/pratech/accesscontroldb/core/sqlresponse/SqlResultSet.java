package com.pratech.accesscontroldb.core.sqlresponse;

import java.io.BufferedReader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oracle.sql.ROWID;

import com.pratech.accesscontroldb.DTO.RequestDTO;
import com.pratech.accesscontroldb.persistence.Store;

/**
 * Procesa el ResulSet
 * 
 * @since 2011-07-01
 * @author Diego Alberto Piedrahita
 * @email dpiedrahita@pratechgroup.com
 * @Company PRATECH S.A.S.
 * 
 */
public class SqlResultSet {

	static final String HEXES = "0123456789ABCDEF";
	
	/**
	 * Recorre un ResultSet y lo pasa a una lista de String[], además se encarga
	 * de la paginación.
	 * 
	 * @param rs
	 * @param requestDTO
	 * @return
	 * @throws SQLException 
	 */
	public List<String[]> getList(ResultSet rs, RequestDTO requestDTO) throws SQLException {
		
		List<String[]> dataLi = new ArrayList<String[]>();

		boolean more = false;
		String[] rowTypes;
		String[] rowTitles;
		String[] row;
		
			try {
			if (requestDTO.getExportData() == 2) {
				more = rs.absolute(1);
			} else {
				more = rs.absolute(requestDTO.getStart());
			}
			} catch (SQLException e) {
				//Esta excepción ocurre cuando se trae un tipo LONG o LONG RAW
				//En estos casos, el driver abre un stream cuando se hace executeQuery o next (o absolute)
				//En este punto el stream esta cerrado y saca una excepción, pero como no interesa leer el
				//campo en este flujo podemos ingnorar la excepción e intentar mover el cursor nuevamente
				if (requestDTO.getExportData() == 2) {
					more = rs.absolute(1);
				} else {
					more = rs.absolute(requestDTO.getStart());
				}
			}
			if (more) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int colCount = rsmd.getColumnCount();

			rowTypes = readTypes(rsmd, colCount);
			dataLi.add(rowTypes);

			rowTitles = readTitles(rsmd, colCount);
			dataLi.add(rowTitles);

			int countAdds = 0;

			while (more) {
				
				row = readRow(rs, requestDTO, colCount);
				dataLi.add(row);

				if (++countAdds == requestDTO.getNumRow()) {
					break;
				}
				more = rs.next();
			}
		}

		return dataLi;
	}

	/**
	 * Lee los encabezados de las columnas del ResultSet.
	 * 
	 * @param rsmd
	 * @param colCount
	 * @return
	 * @throws SQLException
	 */
	public static String[] readTitles(ResultSetMetaData rsmd, int colCount)
			throws SQLException {
		String[] rowTitles;
		rowTitles = new String[colCount + 1];
		rowTitles[0] = "ID";
				for (int i = 1; i <= colCount; i++) {
			rowTitles[i] = rsmd.getColumnName(i);
		}
		return rowTitles;
				}

	/**
	 * Lee los tipos de las columnas del ResultSet.
	 * 
	 * @param rsmd
	 * @param colCount
	 * @return
	 * @throws SQLException
	 */
	public static String[] readTypes(ResultSetMetaData rsmd, int colCount)
			throws SQLException {
		String[] row1 = new String[colCount + 1];
				row1[0] = "ID";
				for (int i = 1; i <= colCount; i++) {
			row1[i] = rsmd.getColumnTypeName(i);
		}
		return row1;
				}

	/**
	 * Lee una fila de un ResultSet y lo pasa a una lista de String[].
	 * 
	 * @param rs
	 * @param requestDTO
	 * @param colCount
	 * @return
	 * @throws SQLException
	 */
	public static String[] readRow(ResultSet rs, RequestDTO requestDTO, int colCount)
			throws SQLException {
					String[] row = new String[colCount + 1];
					row[0] = rs.getRow() + "";
					for (int i = 1; i <= colCount; i++) {
						Object value = rs.getObject(i);
						if (value != null) {
							String name = value.getClass().getCanonicalName();
							if (name.equals("oracle.sql.ROWID")) {
								ROWID rowid = (ROWID) value;
								row[i] = rowid.stringValue();
							} else if (name.equals("oracle.sql.CLOB")) {
								if (requestDTO.getExportData() != 0) {
									try {
										java.sql.Clob cl = (Clob) value;
										StringBuffer strOut = new StringBuffer();
										String aux;
										BufferedReader br = new BufferedReader(
												cl.getCharacterStream());

										/* JJaramillo - 2012-06-26 - Alejandro Marulanda de Sura
										 *                           envió un reemplazo para este while 
										 *                           para corregir un error de exportación de datos
										 *                           CLOB
										while ((aux = br.readLine()) != null) {
											strOut.append(aux);
										}*/
										
										/* JJaramillo - 2012-06-26 - Este fue el reemplazo recibido de Alejandro Marulanda */
                                        while ((aux = br.readLine()) != null) {
                                            aux = aux.replaceAll("\\f|\\b", "");
                                            if(aux != "") {
                                            	strOut.append(aux + "\r\n");
										}
                                        }
										
										row[i] = strOut.toString();
									} catch (Exception e) {
										//Registrar excepción
										e.printStackTrace();
										Store.getInstance().error("NA", "Error SQL al leer el CLOB", e);
									}

								} else {
									row[i] = "CLOB+";
								}														
							} else if (name.equals("oracle.sql.OPAQUE")) {								
								row[i] = "XMLType+";
							} else if (name.equals("java.sql.Timestamp")) {
								Date date = rs.getTimestamp(i);
								// Date date = rs.getDate(i + 1);
								row[i] = date.toString();
							} else if (name.equals("byte[]")) {
								row[i] = getHex((byte[]) value);
							} else {
								row[i] = value.toString();
							}
						} else {
							row[i] = "";
						}
					}
		return row;
	}
	
	public static String getHex( byte [] raw ) {
	    if ( raw == null ) {
	      return null;
	    }
	    final StringBuilder hex = new StringBuilder( 2 * raw.length );
	    for ( final byte b : raw ) {
	      hex.append(HEXES.charAt((b & 0xF0) >> 4))
	         .append(HEXES.charAt((b & 0x0F)));
	    }
	    return hex.toString();
	  }
}
