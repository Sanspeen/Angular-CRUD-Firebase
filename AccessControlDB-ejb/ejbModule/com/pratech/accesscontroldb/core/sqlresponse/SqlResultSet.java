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

import com.pratech.accesscontroldb.DTO.RequestDTO;

import oracle.sql.ROWID;

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

			boolean more;
		String[] rowTypes;
		String[] rowTitles;
		String[] row;
		
			if (requestDTO.getExportData() == 2) {
				more = rs.absolute(1);
			} else {
				more = rs.absolute(requestDTO.getStart());
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
							String name = value.getClass().getName();
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

										while ((aux = br.readLine()) != null) {
											strOut.append(aux);
										}
										row[i] = strOut.toString();
									} catch (Exception ex) {
										Logger.getLogger(
												SqlResultSet.class.getName())
												.log(Level.SEVERE, null, ex);
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
							} else {
								row[i] = value.toString();
							}
						} else {
							row[i] = "";
						}
					}
		return row;
	}
}
