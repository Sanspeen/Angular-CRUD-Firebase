package com.pratech.accesscontroldb.core.ad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import oracle.jdbc.OracleResultSet;
import oracle.sql.CLOB;

import com.pratech.accesscontroldb.DTO.BlocksVariable;
import com.pratech.accesscontroldb.DTO.LogSql;
import com.pratech.accesscontroldb.DTO.RequestDTO;
import com.pratech.accesscontroldb.DTO.ResponseDTO;
import com.pratech.accesscontroldb.util.SqlInstruction;
import com.pratech.accesscontroldb.client.ACDBException;
import com.pratech.accesscontroldb.common.ACConstant;
import com.pratech.accesscontroldb.common.ACUtil;
import com.pratech.accesscontroldb.core.connection.ConnectionDB;
import com.pratech.accesscontroldb.core.sqlresponse.SqlResultSet;
import com.pratech.accesscontroldb.persistence.Store;
import com.pratech.accesscontroldb.persistence.Write;

/**
 * Ejecutas las intrucciones SQL y genera la iformacion para ser grabada en LOG
 * 
 * @since 2011-07-01
 * @author Diego Alberto Piedrahita
 * @email dpiedrahita@pratechgroup.com
 * @Company PRATECH S.A.S.
 * 
 */
public class ExecuteSql {

	/**
	 * Ejecuta las intrucciones SQL.
	 * 
	 * @param requestDTO
	 *            = Informacion de la vista
	 * @param dataInstance
	 *            = datos de la intancias actual.
	 * @return
	 */
	public ResponseDTO executePaginacion(RequestDTO requestDTO,
			Map<String, String> dataInstance) {

		ResponseDTO responseDto = new ResponseDTO();
		Connection con = null;
		Statement statement = null;
		Statement statementExcel = null;
		ResultSet rs = null;
		ResultSet rsExcel = null;
		LogSql logSql = new LogSql();
		boolean moreResults;
		List<String[]> lis = new ArrayList<String[]>(0);

		try {
			con = ConnectionDB.createConnection(dataInstance);
			if (!requestDTO.isSQLServer()) {
				IdentifyClientIdSession.identifyClientIdSession(con,
						dataInstance);
			}
			con.setAutoCommit(false);

			responseDto.setSqlBuffer(printAllExceptions(con.getWarnings()));
			statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			//if (requestDTO.getExportData() != 2) {
				statement.setFetchSize(requestDTO.getNumRow());
			//}
			con.clearWarnings();

			if (!requestDTO.isSQLServer()) {
				JdbcUtil.enable_dbms_output(con, ACConstant.SIZE_BUFFER,
						dataInstance);
			}
			moreResults = statement.execute(requestDTO.getStringSQL());

			if (!requestDTO.isSQLServer()) {
				responseDto = JdbcUtil.print_dbms_output(con, responseDto,
						dataInstance);
			}
			if (moreResults) {
				rs = statement.getResultSet();
				rs.setFetchSize(requestDTO.getNumRow());

				SqlResultSet sqlResultSet = new SqlResultSet();
				lis = sqlResultSet.getList(rs, requestDTO);
				//sqlResultSet.getList() retorna la cantidad
				//de filas de dartos indicada por requestDTO.getNumRow()
				responseDto.setListData(lis);

				responseDto.setTotalRows(rs.getRow());
				if (!(requestDTO.getStart() > 1)) {
					logSql.setTransaccion(dataInstance.get("transaction"));
					logSql.setUsuario(dataInstance.get("analyst"));
					logSql.setDescripcionAudit(requestDTO.getStringSQL());
					logSql.setProceso("Class ExecuteSql, Procedure Execute");
					logSql.setCamposTexto(new String[] {
							dataInstance.get("url"), dataInstance.get("user"),
							"SQL statement executed",
							dataInstance.get("instance"),
							dataInstance.get("scope") });
					logSql.setCod("AC3");
					Store.getInstance().save("3", logSql);
				}

			} else {
				if (statement.getUpdateCount() > 0) {
					con.rollback();

					int rowAfec = statement.executeUpdate(SqlInstruction
							.deleteComments(requestDTO.getStringSQL()));

					String resu = rowAfec + " rows affected.";

					responseDto.setSqlBuffer(resu);

					logSql.setTransaccion(dataInstance.get("transaction"));
					logSql.setUsuario(dataInstance.get("analyst"));
					logSql.setCamposTexto(new String[] {
							dataInstance.get("url"), dataInstance.get("user"),
							resu, dataInstance.get("instance"),
							dataInstance.get("scope") });
					logSql.setDescripcionAudit(requestDTO.getStringSQL());
					logSql.setProceso("Class ExecuteSql, Procedure Execute");
					logSql.setCod("AC2");
					Store.getInstance().save("2", logSql);
				}
			}

			if (!requestDTO.isSQLServer()) {
				JdbcUtil.print_dbms_output(con, responseDto, dataInstance);
			}
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException ex) {
			responseDto.setSqlBuffer(ex.getLocalizedMessage());

			logSql.setTransaccion(dataInstance.get("transaction"));
			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setDescripcionAudit(requestDTO.getStringSQL());
			logSql.setProceso("Class ExecuteSql, Procedure Execute");
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), responseDto.getSqlBuffer(),
					dataInstance.get("instance"), dataInstance.get("scope") });
			logSql.setCod("AC3");
			Store.getInstance().save("3", logSql);
			
			//Registrar excepción
			ex.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error SQL al procesar petición", ex);

			try {
				con.rollback();
			} catch (Exception e) {
				responseDto.setSqlBuffer(e.getLocalizedMessage());

				logSql.setTransaccion(dataInstance.get("transaction"));
				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance"), dataInstance.get("scope") });
				logSql.setDescripcionAudit(requestDTO.getStringSQL());
				logSql.setProceso("Class ExecuteSql, Procedure Execute");
				logSql.setCod("AC3");
				Store.getInstance().save("3", logSql);
				
				//Registrar excepción
				e.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error haciendo rollback de conexión", e);
			}
		} catch (Exception e) {
			responseDto.setSqlBuffer(e.getLocalizedMessage());

			logSql.setTransaccion(dataInstance.get("transaction"));
			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), responseDto.getSqlBuffer(),
					dataInstance.get("instance"), dataInstance.get("scope") });
			logSql.setDescripcionAudit(requestDTO.getStringSQL());
			logSql.setProceso("Class ExecuteSql, Procedure Execute");
			logSql.setCod("AC3");
			Store.getInstance().save("3", logSql);

			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error general al procesar petición", e);
		} finally {
			try {
				statement.close();
			} catch (Exception ex) {
				responseDto.setSqlBuffer(ex.getLocalizedMessage());

				logSql.setTransaccion(dataInstance.get("transaction"));
				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance"), dataInstance.get("scope") });
				logSql.setDescripcionAudit(requestDTO.getStringSQL());
				logSql.setProceso("Class ExecuteSql, Procedure Execute");
				logSql.setCod("AC3");
				Store.getInstance().save("3", logSql);
				
				//Registrar excepción
				ex.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error al cerrar sentencia SQL", ex);
			}
			statement = null;
		}
		if (requestDTO.getExportData() != 0) {
			try {
				Write wri = new Write();
				statementExcel = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				moreResults = statementExcel.execute(requestDTO.getStringSQL());
				rsExcel = statementExcel.getResultSet();
				//Modificacion por Juan Esteban 2012-06-06, fijar un
				//tamaño de exportación de 10 veces el tamaño de página
				//para acelerar el proceso de generación del archivo
				rsExcel.setFetchSize(requestDTO.getNumRow() * 10);
				System.err.println("FETCH SIZE : " + rsExcel.getFetchSize());
				responseDto.setNameFileExport(wri.writeExcel(rsExcel, requestDTO));
			} catch (SQLException e) {
				Logger.getLogger(SqlResultSet.class.getName()).log(Level.SEVERE,
						null, e);
				responseDto.setNameFileExport("<error>");
				responseDto.setSqlBuffer(e.getLocalizedMessage());

				logSql.setTransaccion(dataInstance.get("transaction"));
				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setDescripcionAudit(requestDTO.getStringSQL());
				logSql.setProceso("Class ExecuteSql, Procedure Execute");
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance"), dataInstance.get("scope") });
				logSql.setCod("AC3");
				Store.getInstance().save("3", logSql);
				
				//Registrar excepción
				e.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error SQL al ejecutar consulta para exportar excel", e);
			} catch (Exception e) {
				Logger.getLogger(SqlResultSet.class.getName()).log(Level.SEVERE,
						null, e);
				responseDto.setNameFileExport("<error>");
				responseDto.setSqlBuffer(e.getLocalizedMessage());

				logSql.setTransaccion(dataInstance.get("transaction"));
				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setDescripcionAudit(requestDTO.getStringSQL());
				logSql.setProceso("Class ExecuteSql, Procedure Execute");
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance"), dataInstance.get("scope") });
				logSql.setCod("AC3");
				Store.getInstance().save("3", logSql);
				
				//Registrar excepción
				e.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error general al ejecutar consulta para exportar excel", e);
			} finally {
				try {
					rsExcel.close();
				} catch (SQLException e) {
					responseDto.setSqlBuffer(e.getLocalizedMessage());

				logSql.setTransaccion(dataInstance.get("transaction"));
				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance"), dataInstance.get("scope") });
				logSql.setDescripcionAudit(requestDTO.getStringSQL());
				logSql.setProceso("Class ExecuteSql, Procedure Execute");
				logSql.setCod("AC3");
				Store.getInstance().save("3", logSql);
				
				//Registrar excepción
				e.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error SQL al cerrar conjunto de resultados", e);
				} finally {
					rsExcel = null;
				}
				try {
					statementExcel.close();
				} catch (SQLException e) {
					responseDto.setSqlBuffer(e.getLocalizedMessage());

					logSql.setTransaccion(dataInstance.get("transaction"));
					logSql.setUsuario(dataInstance.get("analyst"));
					logSql.setCamposTexto(new String[] { dataInstance.get("url"),
							dataInstance.get("user"), responseDto.getSqlBuffer(),
							dataInstance.get("instance"), dataInstance.get("scope") });
					logSql.setDescripcionAudit(requestDTO.getStringSQL());
					logSql.setProceso("Class ExecuteSql, Procedure Execute");
					logSql.setCod("AC3");
					Store.getInstance().save("3", logSql);
					
					//Registrar excepción
					e.printStackTrace();
					Store.getInstance().error(dataInstance.get("user"), "Error al cerrar sentencia SQL", e);
				} finally {
					statementExcel = null;
			}
		}
		}
		
		try {
			con.close();
		} catch (Exception ex) {
			responseDto.setSqlBuffer(ex.getLocalizedMessage());

			logSql.setTransaccion(dataInstance.get("transaction"));
			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), responseDto.getSqlBuffer(),
					dataInstance.get("instance"), dataInstance.get("scope") });
			logSql.setDescripcionAudit(requestDTO.getStringSQL());
			logSql.setProceso("Class ExecuteSql, Procedure Execute");
			logSql.setCod("AC3");
			Store.getInstance().save("3", logSql);
			
			//Registrar excepción
			ex.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error al cerrar conexión", ex);
		}
		con = null;
		
		return responseDto;
	}

	/**
	 * Obtiene los mensajes de la base de datos
	 * 
	 * @param sqle
	 * @return
	 */

	public static String printAllExceptions(SQLException sqle) {
		StringBuffer buffer = new StringBuffer();

		while (sqle != null) {
			buffer.append("SQLException : ").append("SQL state: ")
					.append(sqle.getSQLState()).append(" ")
					.append(sqle.toString()).append(" ErrorCode: ")
					.append(sqle.getErrorCode()).append("\n");
			sqle = sqle.getNextException();
		}
		return buffer.toString();
	}

	/**
	 * Actualiza los registro modificados en las celdas
	 * 
	 * @param lisRecord
	 *            - Modified registration list
	 *            <p>
	 *            <ul>
	 *            <li>0-New data</li>
	 *            <li>1-Type data</li>
	 *            <li>2-Name field</li>
	 *            <li>3-String Sql</li>
	 *            <li>4-ROWID</li>
	 *            <li>5-en adelante primaryKey para SQLServer</li>
	 *            </ul>
	 *            </p>
	 * @return
	 * @throws ACDBException 
	 * @see https://forums.oracle.com/forums/thread.jspa?threadID=342798
	 */
	public ResponseDTO UpdateRecords(List<String[]> lisRecord,
			Map<String, String> dataInstance, String SQLSelect) throws ACDBException {
		ResponseDTO responseDto = new ResponseDTO();
		Connection con = null;
		PreparedStatement ps = null;
		LogSql logSql = new LogSql();
		String SQL = "";
		String SQLCronos = "";
		String numberDetail = null;

		try {
			con = ConnectionDB.createConnection(dataInstance);
			IdentifyClientIdSession.identifyClientIdSession(con, dataInstance);
			con.setAutoCommit(false);

			for (String[] listStr : lisRecord) {
				String nameTable = SqlInstruction.getTableFromSql(listStr[4]);
				if (!listStr[2].equals("SYS.XMLTYPE")) {
					SQL = "update " + nameTable.trim() + " set " + listStr[3]
							+ "= ?" + " where ROWID='" + listStr[1] + "'";
				} else {
					SQL = "update " + nameTable.trim() + " set " + listStr[3]
							+ "= XMLType( ? ) where ROWID='" + listStr[1] + "'";
				}

				// Esto para crear la consulta con los valores para ser guardado
				// en cronos
				
				String vec[] = SQL.split("\\?");
				SQLCronos = vec[0];
				SQLCronos += listStr[0].trim();
				SQLCronos += vec[1];
				int romAfe = 0;

				ps = con.prepareStatement(SQL);
				if (listStr[2].equals("NUMBER")) {
					numberDetail = listStr[0].trim();
					BigDecimal bid = new BigDecimal(listStr[0].trim());
					ps.setBigDecimal(1, bid);
				} else if (listStr[2].equals("VARCHAR2")) {
					ps.setString(1, listStr[0].trim());
				} else if (listStr[2].equals("DATE")) {
					String date = listStr[0].trim();
					if (date.length() == 10) {
						date = date + " 00:00:00.0";
					}
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss.SSS");
					java.util.Date dat = sdf.parse(date);
					java.sql.Timestamp sqlFec = new java.sql.Timestamp(
							dat.getTime());

					ps.setTimestamp(1, sqlFec);
				} else if (listStr[2].equals("CLOB")
						|| listStr[2].equals("SYS.XMLTYPE")) {
					CLOB tempClob = null;
					try {
						// Se inicia el CLOB
						tempClob = CLOB.createTemporary(con, true,
								CLOB.DURATION_SESSION);
						// Se abre el CLOB en modo escritura
						tempClob.open(CLOB.MODE_READWRITE);
						// Se obtiene el flujo de salida para escribir
						Writer tempClobWriter = tempClob.setCharacterStream(0);
						// Se escribe el dato en el CLOB temporal
						tempClobWriter.write(listStr[0].trim());

						tempClobWriter.flush();
						tempClobWriter.close();

						// Cerrar el clob temporal
						tempClob.close();
					} catch (SQLException sqlexp) {
						tempClob.freeTemporary();
						sqlexp.printStackTrace();
						
						//Registrar excepción
						sqlexp.printStackTrace();
						Store.getInstance().error(dataInstance.get("user"), "Error SQL al escribir CLOB temporal", sqlexp);
					} catch (Exception exp) {
						tempClob.freeTemporary();
						exp.printStackTrace();
						
						//Registrar excepción
						exp.printStackTrace();
						Store.getInstance().error(dataInstance.get("user"), "Error general al escribir CLOB temporal", exp);
					}
					ps.setClob(1, tempClob);
				}
				ps.executeUpdate();
				romAfe = ps.getUpdateCount();

				if (romAfe > 0) {
					responseDto.setSqlBuffer("Succesfully modified records");
				}
				logSql.setTransaccion(dataInstance.get("transaction"));
				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setDescripcionAudit(SQLCronos + "; " + SQLSelect);
				logSql.setProceso("Class ExecuteSql, Procedure UpdateRecords");
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), "SQL statement executed",
						dataInstance.get("instance"), dataInstance.get("scope") });
				logSql.setCod("AC2");
				Store.getInstance().save("2", logSql);
			}
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException ex) {
			responseDto.setSqlBuffer(ex.getLocalizedMessage());

			logSql.setTransaccion(dataInstance.get("transaction"));
			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setDescripcionAudit(SQLCronos + "; " + SQLSelect);
			logSql.setProceso("Class ExecuteSql, Procedure UpdateRecords");
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), responseDto.getSqlBuffer(),
					dataInstance.get("instance"), dataInstance.get("scope") });
			logSql.setCod("AC3");
			Store.getInstance().save("3", logSql);
			
			//Registrar excepción
			ex.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error SQL durante la actualización de registros", ex);

			try {
				con.rollback();
			} catch (Exception e) {
				responseDto.setSqlBuffer(e.getLocalizedMessage());

				logSql.setTransaccion(dataInstance.get("transaction"));
				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance"), dataInstance.get("scope") });
				logSql.setDescripcionAudit(SQLCronos + ";" + SQLSelect);
				logSql.setProceso("Class ExecuteSql, Procedure UpdateRecords");
				logSql.setCod("AC3");
				Store.getInstance().save("3", logSql);
				
				//Registrar excepción
				e.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error al hacer rollback de la transacción", e);
			}
		} catch (NumberFormatException e) {
				responseDto.setSqlBuffer("Unparseable number: \"" + numberDetail + "\"");
			
			logSql.setTransaccion(dataInstance.get("transaction"));
			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), responseDto.getSqlBuffer(),
					dataInstance.get("instance"), dataInstance.get("scope") });
			logSql.setDescripcionAudit(SQLCronos + ";" + SQLSelect);
			logSql.setProceso("Class ExecuteSql, Procedure UpdateRecords");
			logSql.setCod("AC3");
			Store.getInstance().save("3", logSql);

			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error de usuario en formato de número", e);
			
		} catch (ParseException e) {
			responseDto.setSqlBuffer(e.getLocalizedMessage());

			logSql.setTransaccion(dataInstance.get("transaction"));
			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), responseDto.getSqlBuffer(),
					dataInstance.get("instance"), dataInstance.get("scope") });
			logSql.setDescripcionAudit(SQLCronos + ";" + SQLSelect);
			logSql.setProceso("Class ExecuteSql, Procedure UpdateRecords");
			logSql.setCod("AC3");
			Store.getInstance().save("3", logSql);

			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error de usuario en formato de fecha", e);
			
		} finally {
			try {
				ps.close();
			} catch (Exception ex) {
				responseDto.setSqlBuffer(ex.getLocalizedMessage());

				logSql.setTransaccion(dataInstance.get("transaction"));
				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance"), dataInstance.get("scope") });
				logSql.setDescripcionAudit(SQLCronos + ";" + SQLSelect);
				logSql.setProceso("Class ExecuteSql, Procedure UpdateRecords");
				logSql.setCod("AC3");
				Store.getInstance().save("3", logSql);
				
				//Registrar excepción
				ex.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error al cerrar una sentencia preparada", ex);
			}
			ps = null;
			try {
				con.close();
			} catch (Exception ex) {
				responseDto.setSqlBuffer(ex.getLocalizedMessage());

				logSql.setTransaccion(dataInstance.get("transaction"));
				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance"), dataInstance.get("scope") });
				logSql.setDescripcionAudit(SQLCronos + ";" + SQLSelect);
				logSql.setProceso("Class ExecuteSql, Procedure UpdateRecords");
				logSql.setCod("AC3");
				Store.getInstance().save("3", logSql);
				
				//Registrar excepción
				ex.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error al cerrar una conexión", ex);
			}
			con = null;
		}
		return responseDto;
	}

	/**
	 * Retorna la informacion de un tipo de datos CLOB
	 * 
	 * vector positions 0 - Field name 1 - New data 2 - Table name
	 * 
	 * @param parameters
	 * @return
	 */
	public String getCLOB(String[] parameters, Map<String, String> dataInstance) throws ACDBException {
		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		String result = "";
		LogSql logSql = new LogSql();
		boolean moreResult;
		System.out.println(parameters[0] + " 1");

		try {
			con = ConnectionDB.createConnection(dataInstance);
			System.out.println("Conexion");
			if (dataInstance.get("url").indexOf("sqlserver") < 0) {
				IdentifyClientIdSession.identifyClientIdSession(con,
						dataInstance);
			}
			statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			moreResult = statement.execute(parameters[2]);
			if (moreResult) {
				rs = statement.getResultSet();
				rs.absolute(Integer.parseInt(parameters[1]));
			}

			java.sql.Clob cl = rs.getClob(parameters[0]);
			StringBuffer strOut = new StringBuffer();
			String aux;
			BufferedReader br = new BufferedReader(cl.getCharacterStream());

			while ((aux = br.readLine()) != null) {
				strOut.append(aux + "\r\n");
			}
			result = strOut.toString();
			System.out.println("paso");

		} catch (SQLException ex) {
			logSql.setTransaccion(dataInstance.get("transaction"));
			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setDescripcionAudit(ex.getLocalizedMessage());
			logSql.setProceso("Class ExecuteSql, Procedure UpdateRecords");
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), "", dataInstance.get("instance"),
					dataInstance.get("scope") });
			logSql.setCod("AC3");
			Store.getInstance().save("3", logSql);
			
			//Registrar excepción
			ex.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error SQL al leer CLOB", ex);
		} catch (IOException ex) {
			//Registrar excepción
			ex.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error I/O al leer CLOB", ex);
		} finally {
			try {
				statement.close();
			} catch (Exception ex) {
				//Registrar excepción
				ex.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error al cerrar sentencia", ex);
			}
			statement = null;
			try {
				con.close();
			} catch (Exception ex) {
				//Registrar excepción
				ex.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error al cerrar conexión", ex);
			}
			con = null;
			try {
				rs.close();
			} catch (Exception ex) {
				//Registrar excepción
				ex.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error al cerrar conjunto de resultados", ex);
			}
			rs = null;
		}
		System.out.println(result);
		return result;
	}

	/**
	 * Retorna la informacion de un tipo de datos CLOB
	 * 
	 * vector positions 0 - Field name 1 - New data 2 - Table name
	 * 
	 * @param parameters
	 * @return
	 */
	public String getXMLType(String[] parameters,
			Map<String, String> dataInstance) throws ACDBException {
		Connection con = null;
		ResultSet rs = null;
		String result = "";
		LogSql logSql = new LogSql();

		try {
			con = ConnectionDB.createConnection(dataInstance);
			if (dataInstance.get("url").indexOf("sqlserver") < 0) {
				IdentifyClientIdSession.identifyClientIdSession(con,
						dataInstance);
			}
			Statement statement = con.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			String sql1 = parameters[2].toUpperCase()
					.replaceAll("[\n\r\t]", " ")
					.replaceAll("^(SELECT)\\s+.*\\s+(FROM)", "");

			StringTokenizer tokens = new StringTokenizer(sql1);

			Vector<String> vectorSQL = new Vector<String>();
			while (tokens.hasMoreTokens()) {
				vectorSQL.add(tokens.nextToken());
			}

			String tbl = "t";
			if (vectorSQL.size() > 1) {
				if (vectorSQL.get(1).length() < 4) {
					tbl = vectorSQL.get(1).trim();
				} else {
					vectorSQL.add(1, tbl);
				}
			} else {
				vectorSQL.add(1, tbl);
			}

			String sql2 = "select " + tbl + "." + parameters[0].trim()
					+ ".getClobVal()" + " from ";

			vectorSQL.add(0, sql2);

			String stringSQL = "";
			for (Iterator<String> iterator = vectorSQL.iterator(); iterator
					.hasNext();) {
				stringSQL += iterator.next();
				stringSQL += " ";
			}

			rs = statement.executeQuery(stringSQL);
			OracleResultSet orset = (OracleResultSet) rs;
			orset.absolute(Integer.parseInt(parameters[1]));

			java.sql.Clob cl = orset.getClob(1);

			StringBuffer strOut = new StringBuffer();
			String aux;
			BufferedReader br = new BufferedReader(cl.getCharacterStream());

			while ((aux = br.readLine()) != null) {
				strOut.append(aux + "\r\n");
			}
			result = strOut.toString();

		} catch (SQLException ex) {
			logSql.setTransaccion(dataInstance.get("transaction"));
			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setDescripcionAudit(ex.getLocalizedMessage());
			logSql.setProceso("Class ExecuteSql, Procedure UpdateRecords");
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), "", dataInstance.get("instance"),
					dataInstance.get("scope") });
			logSql.setCod("AC3");
			Store.getInstance().save("3", logSql);
			
			//Registrar excepción
			ex.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error SQL al leer XML", ex);
		} catch (IOException ex) {
			//Registrar excepción
			ex.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error I/O al leer XML", ex);
		} finally {
			try {
				rs.close();
			} catch (Exception ex) {
				//Registrar excepción
				ex.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error al cerrar conjunto de resultados", ex);
			}
			rs = null;
			try {
				con.close();
			} catch (Exception ex) {
				//Registrar excepción
				ex.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error al cerrar conexión", ex);
			}
			con = null;
		}
		return result;
	}

	/**
	 * Ejecuta los store procedure
	 * 
	 * @param requestDTO
	 * @param lisBlo
	 * @return
	 */
	public ResponseDTO executeSP(RequestDTO requestDTO,
			List<BlocksVariable> lisBlo, Map<String, String> dataInstance) throws ACDBException {

		String SQL = SqlInstruction.replaceTextBlock(requestDTO.getStringSQL(),
				lisBlo);
		CallableStatement cstmt = null;
		ResponseDTO responseDto = new ResponseDTO();
		ResultSet rs = null;
		Connection con = null;
		LogSql logSql = new LogSql();

		String variBloc = " - Varibles utilizadas";
		for (BlocksVariable lis : lisBlo) {
			variBloc += " Var (";
			variBloc += lis.getVariable();
			variBloc += ") Type (";
			variBloc += lis.getTypeData();
			variBloc += ") In/Out (";
			variBloc += lis.getAddress();
			variBloc += ") Values (";
			variBloc += lis.getValue() + ");";
		}

		List<String[]> listData = new ArrayList<String[]>(0);

		listData.add(new String[] { "", "", "Valores" });
		listData.add(new String[] { "", "", "Valores" });

		try {
			con = ConnectionDB.createConnection(dataInstance);
			if (dataInstance.get("url").indexOf("sqlserver") < 0) {
				IdentifyClientIdSession.identifyClientIdSession(con,
						dataInstance);
			}
			con.setAutoCommit(false);
			responseDto.setSqlBuffer(printAllExceptions(con.getWarnings()));
			con.clearWarnings();

			JdbcUtil.enable_dbms_output(con, ACConstant.SIZE_BUFFER,
					dataInstance);

			cstmt = con.prepareCall(SqlInstruction.deleteComments(SQL));

			int i = 0;
			for (BlocksVariable b : lisBlo) {
				if (b.isUse()
						&& (b.getAddress().equals("OUT") || b.getAddress()
								.equals("INOUT"))) {
					i++;
					cstmt.registerOutParameter(i,
							ACUtil.getType(b.getTypeData()));
				}
			}
			cstmt.execute();

			i = 0;
			for (BlocksVariable b : lisBlo) {
				if (b.isUse()
						&& (b.getAddress().equals("OUT") || b.getAddress()
								.equals("INOUT"))) {
					i++;
					listData.add(new String[] { "", b.getVariable(),
							cstmt.getString(i) });
				}
			}

			responseDto = JdbcUtil.print_dbms_output(con, responseDto,
					dataInstance);
			responseDto.setListData(listData);

			responseDto.setTotalRows(i);

			logSql.setTransaccion(dataInstance.get("transaction"));
			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setDescripcionAudit(requestDTO.getStringSQL() + variBloc);
			logSql.setProceso("Class ExecuteSql, Procedure executeSP");
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), "SQL statement executed",
					dataInstance.get("instance"), dataInstance.get("scope") });
			logSql.setCod("AC3");
			Store.getInstance().save("3", logSql);

		} catch (SQLException e) {
			responseDto.setSqlBuffer(e.getLocalizedMessage());

			logSql.setTransaccion(dataInstance.get("transaction"));
			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setDescripcionAudit(requestDTO.getStringSQL() + variBloc);
			logSql.setProceso("Class ExecuteSql, Procedure UpdateRecords");
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), responseDto.getSqlBuffer(),
					dataInstance.get("instance"), dataInstance.get("scope") });
			logSql.setCod("AC3");
			Store.getInstance().save("3", logSql);
			
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error al cerrar conexión", e);
		} finally {
			try {
				cstmt.close();
			} catch (Exception ex) {
				responseDto.setSqlBuffer(ex.getLocalizedMessage());

				logSql.setTransaccion(dataInstance.get("transaction"));
				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance"), dataInstance.get("scope") });
				logSql.setDescripcionAudit(requestDTO.getStringSQL() + variBloc);
				logSql.setProceso("Class ExecuteSql, Procedure Execute");
				logSql.setCod("AC3");
				Store.getInstance().save("3", logSql);
				
				//Registrar excepción
				ex.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error al cerrar sentencia invocable", ex);
			}
			cstmt = null;
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception ex) {
				responseDto.setSqlBuffer(ex.getLocalizedMessage());

				logSql.setTransaccion(dataInstance.get("transaction"));
				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance"), dataInstance.get("scope") });
				logSql.setDescripcionAudit(requestDTO.getStringSQL() + variBloc);
				logSql.setProceso("Class ExecuteSql, Procedure Execute");
				logSql.setCod("AC3");
				Store.getInstance().save("3", logSql);
				
				//Registrar excepción
				ex.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error al cerrar conjunto de resultados", ex);
			}
			rs = null;
			try {
				con.close();
			} catch (Exception ex) {
				responseDto.setSqlBuffer(ex.getLocalizedMessage());

				logSql.setTransaccion(dataInstance.get("transaction"));
				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance"), dataInstance.get("scope") });
				logSql.setDescripcionAudit(requestDTO.getStringSQL() + variBloc);
				logSql.setProceso("Class ExecuteSql, Procedure Execute");
				logSql.setCod("AC3");
				Store.getInstance().save("3", logSql);
				
				//Registrar excepción
				ex.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error al cerrar conexión", ex);
			}
			con = null;
		}
		return responseDto;
	}

	public ResponseDTO executeBlock(RequestDTO requestDTO,
			Map<String, String> dataInstance) {
		ResponseDTO responseDto = new ResponseDTO();
		Connection con = null;
		Statement statement = null;
		LogSql logSql = new LogSql();

		try {
			con = ConnectionDB.createConnection(dataInstance);
			if (dataInstance.get("url").indexOf("sqlserver") < 0) {
				IdentifyClientIdSession.identifyClientIdSession(con,
						dataInstance);
			}
			con.setAutoCommit(false);

			responseDto.setSqlBuffer(printAllExceptions(con.getWarnings()));
			statement = con.createStatement();
			con.clearWarnings();
			List<String[]> lisTem = new ArrayList<String[]>(0);

			for (int i = 0; i < requestDTO.getBlockSQL().length; i++) {
				try {
					statement.execute(requestDTO.getBlockSQL()[i]);

					if (!requestDTO.isCommitBlock()) {
						lisTem.add(new String[] { requestDTO.getBlockSQL()[i],
								"OK" });
					}

				} catch (SQLException e) {
					if (requestDTO.isCommitBlock()) {
						responseDto.setSqlBuffer(e.getLocalizedMessage());
						
						//Registrar excepción
						e.printStackTrace();
						Store.getInstance().error(dataInstance.get("user"), "Error al ejecutar sentencias en bloque", e);
					} else {
						lisTem.add(new String[] {
								SqlInstruction.deleteComments(requestDTO
										.getBlockSQL()[i]),
								e.getLocalizedMessage() });
					}
				}
			}
			if (requestDTO.isCommitBlock()) {
				con.commit();
				con.setAutoCommit(true);
				String resu = "";
				for (int i = 0; i < requestDTO.getBlockSQL().length; i++) {
					resu += requestDTO.getBlockSQL()[i] + "; ";
				}
				if (requestDTO.getBlockSQL().length > 0) {
				responseDto
							.setSqlBuffer("Instructions were executed correctly");
				logSql.setTransaccion(dataInstance.get("transaction"));
				logSql.setUsuario(dataInstance.get("analyst"));
					logSql.setCamposTexto(new String[] {
							dataInstance.get("url"), dataInstance.get("user"),
							responseDto.getSqlBuffer(),
							dataInstance.get("instance"),
							dataInstance.get("scope") });
				logSql.setDescripcionAudit(resu);
				logSql.setProceso("Class ExecuteSql, Procedure Execute");
				logSql.setCod("AC2");
				Store.getInstance().save("2", logSql);
			} else {
					responseDto
							.setSqlBuffer("All instructions were executed correctly");
				}

			} else {
				responseDto.setListBlockSQL(lisTem);
				con.rollback();
			}
		} catch (SQLException ex) {
			responseDto.setSqlBuffer(ex.getLocalizedMessage());

			logSql.setTransaccion(dataInstance.get("transaction"));
			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setDescripcionAudit(requestDTO.getStringSQL());
			logSql.setProceso("Class ExecuteSql, Procedure executeBlock");
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), responseDto.getSqlBuffer(),
					dataInstance.get("instance"),dataInstance.get("scope") });
			logSql.setCod("AC3");
			Store.getInstance().save("3", logSql);
			
			//Registrar excepción
			ex.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error SQL al ejecutar sentencias en bloque", ex);

			try {
				con.rollback();
			} catch (Exception e) {
				responseDto.setSqlBuffer(e.getLocalizedMessage());

				logSql.setTransaccion(dataInstance.get("transaction"));
				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance"), dataInstance.get("scope") });
				logSql.setDescripcionAudit(requestDTO.getStringSQL());
				logSql.setProceso("Class ExecuteSql, Procedure executeBlock");
				logSql.setCod("AC3");
				Store.getInstance().save("3", logSql);
				
				//Registrar excepción
				e.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error al ejecutar rollback", e);
			}
		} catch (Exception e) {
			responseDto.setSqlBuffer(e.getLocalizedMessage());

			logSql.setTransaccion(dataInstance.get("transaction"));
			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), responseDto.getSqlBuffer(),
					dataInstance.get("instance"), dataInstance.get("scope") });
			logSql.setDescripcionAudit(requestDTO.getStringSQL());
			logSql.setProceso("Class ExecuteSql, Procedure executeBlock");
			logSql.setCod("AC3");
			Store.getInstance().save("1", logSql);

			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error(dataInstance.get("user"), "Error general al ejecutar sentencias en bloque", e);
		} finally {
			try {
				statement.close();
			} catch (Exception ex) {
				responseDto.setSqlBuffer(ex.getLocalizedMessage());

				logSql.setTransaccion(dataInstance.get("transaction"));
				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance"), dataInstance.get("scope") });
				logSql.setDescripcionAudit(requestDTO.getStringSQL());
				logSql.setProceso("Class ExecuteSql, Procedure executeBlock");
				logSql.setCod("AC3");
				Store.getInstance().save("3", logSql);
				
				//Registrar excepción
				ex.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error al cerrar sentencia", ex);
			}
			statement = null;
			try {
				con.close();
			} catch (Exception ex) {
				responseDto.setSqlBuffer(ex.getLocalizedMessage());

				logSql.setTransaccion(dataInstance.get("transaction"));
				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance"),dataInstance.get("scope") });
				logSql.setDescripcionAudit(requestDTO.getStringSQL());
				logSql.setProceso("Class ExecuteSql, Procedure executeBlock");
				logSql.setCod("AC3");
				Store.getInstance().save("3", logSql);
				
				//Registrar excepción
				ex.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error general al cerrar conexión", ex);
			}
			con = null;
		}
		return responseDto;
	}
	
	// Modificado el 2012-05-24 por Juan
	public ResponseDTO executeExplainPlan(RequestDTO requestDTO,
			Map<String, String> dataInstance) throws ACDBException {
		ResponseDTO responseDto = new ResponseDTO();
		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		LogSql logSql = new LogSql();
		boolean moreResults;
		List<String[]> lis = new ArrayList<String[]>(0);
		String realQuery = requestDTO.getStringSQL();
		StringBuffer oracleResult = null; //Resultado para Oracle va en texto
		
		if (requestDTO.isExplainPlan() && requestDTO.getTypeSql() == 3) {
			try {
				con = ConnectionDB.createConnection(dataInstance);

				if (!requestDTO.isSQLServer()) {
					IdentifyClientIdSession.identifyClientIdSession(con,
							dataInstance);
				}
				con.setAutoCommit(false);

				responseDto.setSqlBuffer(printAllExceptions(con.getWarnings()));
				statement = con.createStatement();
				// statement.setFetchSize(requestDTO.getNumRow());

				con.clearWarnings();

				// Modificado el 2012-05-28 por Juan
				// ID Mantis 0003693
				if (realQuery.trim().indexOf(";") >= 0) {
					String tem = realQuery.trim();
					if(tem.endsWith(";")){
						tem = tem.substring(0,tem.length()-1);	
					}					
					requestDTO.setStringSQL(tem);
			}

				// validacion si es un SqlServer para ejecutar el explain plan
				if (!requestDTO.isSQLServer()) {
					JdbcUtil.enable_dbms_output(con, ACConstant.SIZE_BUFFER,
							dataInstance);

					requestDTO.setStringSQL("explain plan for "
							+ requestDTO.getStringSQL());

				} else {
					// es sql Server
					int numero = statement.executeUpdate("SET SHOWPLAN_ALL ON");
					if (numero == 0) {

					}
					// requestDTO.setStringSQL("SET SHOWPLAN_ALL ON;GO "+requestDTO.getStringSQL());
				}
				moreResults = statement.execute(requestDTO.getStringSQL());
				// moreResults=statement.getMoreResults();
				if (!requestDTO.isSQLServer()) {

					moreResults = statement
							.execute("select plan_table_output from table(dbms_xplan.display())");

					responseDto = JdbcUtil.print_dbms_output(con, responseDto,
							dataInstance);
				}
				
				//Obtener los resultados de acuerdo al motor...
				//Oracle -> texto
				//SQLServer -> tabla
				if (requestDTO.isSQLServer()) {
					//Obtener resultados para SQLServer (los resultados van como una tabla)
					if (moreResults) {
						rs = statement.getResultSet();
						// aqui se devuelve los datos para la tabla
						rs.setFetchSize(requestDTO.getNumRow());
	
						// SqlResultSet sqlResultSet = new SqlResultSet();
						// lis = sqlResultSet.getList(rs, requestDTO);
						// sqlResultSet.getList() retorna la cantidad
						// de filas de dartos indicada por requestDTO.getNumRow()
						String[] rowTypes;
						String[] rowTitles;
						String[] row;
						ResultSetMetaData rsmd = rs.getMetaData();
						int colCount = rsmd.getColumnCount();
	
						rowTypes = SqlResultSet.readTypes(rsmd, colCount);
						lis.add(rowTypes);
	
						rowTitles = SqlResultSet.readTitles(rsmd, colCount);
						lis.add(rowTitles);
						while (rs.next()) {
	
							row = SqlResultSet.readRow(rs, requestDTO, colCount);
							lis.add(row);
							for (String r : row)
							{
								r.replaceAll("[\\s]", "&nbsp;");
							}
						}
						responseDto.setListData(lis);
	
						responseDto.setTotalRows(rs.getRow());
						rs.close();

						statement.executeUpdate("SET SHOWPLAN_ALL OFF");
					}
				} else {
					//Obtener resultados para Oracle (los resultados van como un texto)
					oracleResult = new StringBuffer();
					
					if (moreResults) {
						rs = statement.getResultSet();
						rs.setFetchSize(requestDTO.getNumRow());
	
						while (rs.next()) {
							oracleResult.append(rs.getString(1));
							oracleResult.append("\r\n");
						}
						responseDto.setSqlBuffer(oracleResult.toString());
						rs.close();
					}
				}
				logSql.setTransaccion(dataInstance.get("transaction"));
				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setDescripcionAudit(realQuery);
				logSql.setProceso("Class ExecuteSql, Procedure executeExplainPlan");
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), "Explain plan executed",
						dataInstance.get("instance"), dataInstance.get("scope") });
				logSql.setCod("AC5");
				Store.getInstance().save("5", logSql);
			} catch (SQLException e) {
				responseDto.setSqlBuffer(e.getLocalizedMessage());

				logSql.setTransaccion(dataInstance.get("transaction"));
				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setDescripcionAudit(realQuery);
				logSql.setProceso("Class ExecuteSql, Procedure executeExplainPlan");
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance"), dataInstance.get("scope") });
				logSql.setCod("AC5");
				Store.getInstance().save("5", logSql);

				// Registrar excepción
				e.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"),
						"Error al obtener el Explain plan", e);
			} finally {
				if (statement != null) {
					try {
						statement.close();
						statement = null;
					} catch (SQLException e) {
						responseDto.setSqlBuffer(e.getLocalizedMessage());

						logSql.setTransaccion(dataInstance.get("transaction"));
						logSql.setUsuario(dataInstance.get("analyst"));
						logSql.setDescripcionAudit(realQuery);
						logSql.setProceso("Class ExecuteSql, Procedure executeExplainPlan");
						logSql.setCamposTexto(new String[] {
								dataInstance.get("url"),
								dataInstance.get("user"),
								responseDto.getSqlBuffer(),
								dataInstance.get("instance"),
								dataInstance.get("scope") });
						logSql.setCod("AC5");
						Store.getInstance().save("5", logSql);

						// Registrar excepción
						e.printStackTrace();
						Store.getInstance().error(dataInstance.get("user"),
								"Error al cerrar el stament explain plan", e);
					}
				}
				if (con != null) {
					try {
						con.close();
			con = null;
					} catch (SQLException e) {
						responseDto.setSqlBuffer(e.getLocalizedMessage());

						logSql.setTransaccion(dataInstance.get("transaction"));
						logSql.setUsuario(dataInstance.get("analyst"));
						logSql.setDescripcionAudit(realQuery);
						logSql.setProceso("Class ExecuteSql, Procedure executeExplainPlan");
						logSql.setCamposTexto(new String[] {
								dataInstance.get("url"),
								dataInstance.get("user"),
								responseDto.getSqlBuffer(),
								dataInstance.get("instance"),
								dataInstance.get("scope") });
						logSql.setCod("AC5");
						Store.getInstance().save("5", logSql);

						// Registrar excepción
						e.printStackTrace();
						Store.getInstance().error(dataInstance.get("user"),
								"Error al cerrar la conexion explain plan", e);
		}
				}
			}
		}

		return responseDto;
	}
}