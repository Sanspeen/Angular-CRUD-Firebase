package com.pratech.accesscontroldb.core.sqlresponse;

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
	 * Recorre un ResultSet y lo pasa a una lista de String[], ademas se encarga
	 * de la paginacion.
	 * 
	 * @param rs
	 * @param requestDTO
	 * @return
	 */
	public List<String[]> getList(ResultSet rs, RequestDTO requestDTO) {
		List<String[]> dataLi = new ArrayList<String[]>();
		try {
			boolean more = rs.absolute(requestDTO.getStart());
			if (more) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int colCount = rsmd.getColumnCount();

				String[] row1 = new String[colCount + 1];
				row1[0] = "ID";
				for (int i = 1; i <= colCount; i++) {
					row1[i] = rsmd.getColumnTypeName(i);
				}
				dataLi.add(row1);

				row1 = new String[colCount + 1];
				row1[0] = "ID";
				for (int i = 1; i <= colCount; i++) {
					row1[i] = rsmd.getColumnName(i);
				}
				dataLi.add(row1);

				int countAdds = 0;

				while (more) {
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
								row[i] = "CLOB+";
								// } else if (name.equals("oracle.sql.OPAQUE"))
								// {
								// OracleResultSet orset = (OracleResultSet) rs;
								// XMLType poxml =
								// XMLType.createXML(orset.getOPAQUE(i));
								// row[i] = poxml.getStringVal();
							} else if (name.equals("java.sql.Timestamp")) {
								Date date = rs.getTimestamp(i);
								// Date date = rs.getDate(i + 1);
								row[i] = date.toString().substring(0, 10);
							} else {
								row[i] = value.toString();
							}
						} else {
							row[i] = "";
						}
					}
					dataLi.add(row);

					if (++countAdds == requestDTO.getNumRow()) {
						break;
					}
					more = rs.next();
				}
			}

		} catch (SQLException ex) {
			Logger.getLogger(SqlResultSet.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return dataLi;
	}
}
