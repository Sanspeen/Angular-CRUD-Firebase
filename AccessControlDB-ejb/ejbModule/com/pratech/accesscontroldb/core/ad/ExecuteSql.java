package com.pratech.accesscontroldb.core.ad;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pratech.accesscontroldb.DTO.BlocksVariable;
import com.pratech.accesscontroldb.DTO.LogSql;
import com.pratech.accesscontroldb.DTO.RequestDTO;
import com.pratech.accesscontroldb.DTO.ResponseDTO;
import com.pratech.accesscontroldb.util.SqlInstruction;
import com.pratech.accesscontroldb.common.ACConstant;
import com.pratech.accesscontroldb.common.ACUtil;
import com.pratech.accesscontroldb.core.connection.ConnectionDB;
import com.pratech.accesscontroldb.core.sqlresponse.SqlResultSet;
import com.pratech.accesscontroldb.persistence.Store;

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
		ResultSet rs = null;
		LogSql logSql = new LogSql();
		Store store = new Store();
		boolean moreResults;

		try {
			con = ConnectionDB.lastConnection(dataInstance);
			con.setAutoCommit(false);

			responseDto.setSqlBuffer(printAllExceptions(con.getWarnings()));
			statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			statement.setFetchSize(requestDTO.getNumRow());
			con.clearWarnings();

			JdbcUtil.enable_dbms_output(con, ACConstant.SIZE_BUFFER,
					dataInstance);

			moreResults = statement.execute(requestDTO.getStringSQL());

			responseDto = JdbcUtil.print_dbms_output(con, responseDto,
					dataInstance);

			if (moreResults) {
				rs = statement.getResultSet();
				rs.setFetchSize(requestDTO.getNumRow());

				List<String[]> lis = new ArrayList<String[]>();
				SqlResultSet sqlResultSet = new SqlResultSet();
				lis = sqlResultSet.getList(rs, requestDTO);
				responseDto.setListData(lis);

				responseDto.setTotalRows(rs.getRow());
				if (!(requestDTO.getStart() > 1)) {
					logSql.setUsuario(dataInstance.get("analyst"));
					logSql.setCamposTexto(new String[] {
							dataInstance.get("url"), dataInstance.get("user") });
					logSql.setDescripcionAudit(requestDTO.getStringSQL());
					logSql.setProceso("Class ExecuteSql, Procedure Execute");
					logSql.setCamposTexto(new String[] {
							dataInstance.get("url"), dataInstance.get("user"),
							"SQL statement executed",
							dataInstance.get("instance") });
					logSql.setCod("AC3");
					store.save("3", logSql);
				}

			} else {
				if (statement.getUpdateCount() > 0) {
					String resu = statement.getUpdateCount()
							+ " rows affected.";
					responseDto.setSqlBuffer(resu);

					logSql.setUsuario(dataInstance.get("analyst"));
					logSql.setCamposTexto(new String[] {
							dataInstance.get("url"), dataInstance.get("user"),
							requestDTO.getStringSQL(),
							dataInstance.get("instance") });
					logSql.setDescripcionAudit(resu);
					logSql.setProceso("Class ExecuteSql, Procedure Execute");
					logSql.setCod("AC2");
					store.save("2", logSql);
				}
			}
			JdbcUtil.print_dbms_output(con, responseDto, dataInstance);

			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException ex) {
			responseDto.setSqlBuffer(ex.getLocalizedMessage());

			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user") });
			logSql.setDescripcionAudit(requestDTO.getStringSQL());
			logSql.setProceso("Class ExecuteSql, Procedure Execute");
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), responseDto.getSqlBuffer(),
					dataInstance.get("instance") });
			logSql.setCod("AC3");
			store.save("3", logSql);

			try {
				con.rollback();
			} catch (Exception e) {
				responseDto.setSqlBuffer(e.getLocalizedMessage());

				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance") });
				logSql.setDescripcionAudit(requestDTO.getStringSQL());
				logSql.setProceso("Class ExecuteSql, Procedure Execute");
				logSql.setCod("AC3");
				store.save("1", logSql);
			}
		} catch (Exception e) {
			responseDto.setSqlBuffer(e.getLocalizedMessage());

			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), responseDto.getSqlBuffer(),
					dataInstance.get("instance") });
			logSql.setDescripcionAudit(requestDTO.getStringSQL());
			logSql.setProceso("Class ExecuteSql, Procedure Execute");
			logSql.setCod("AC3");
			store.save("1", logSql);

			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (Exception ex) {
				responseDto.setSqlBuffer(ex.getLocalizedMessage());

				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance") });
				logSql.setDescripcionAudit(requestDTO.getStringSQL());
				logSql.setProceso("Class ExecuteSql, Procedure Execute");
				logSql.setCod("AC3");
				store.save("1", logSql);
			}
			statement = null;
			try {
				con.close();
			} catch (Exception ex) {
				responseDto.setSqlBuffer(ex.getLocalizedMessage());

				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance") });
				logSql.setDescripcionAudit(requestDTO.getStringSQL());
				logSql.setProceso("Class ExecuteSql, Procedure Execute");
				logSql.setCod("AC3");
				store.save("1", logSql);
			}
			con = null;
		}
		return responseDto;
	}

	/**
	 * Obtiene los mensajes de la base de datos
	 * 
	 * @param sqle
	 * @return
	 */

	private String printAllExceptions(SQLException sqle) {
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
	 *            <li>1-ROWID</li>
	 *            <li>2-Type data</li>
	 *            <li>3-Name field</li>
	 *            <li>4-String Sql</li>
	 *            </ul>
	 *            </p>
	 * @return
	 */
	public ResponseDTO UpdateRecords(List<String[]> lisRecord,
			Map<String, String> dataInstance, String SQLSelect) {
		ResponseDTO responseDto = new ResponseDTO();
		Connection con = null;
		PreparedStatement ps = null;
		LogSql logSql = new LogSql();
		Store store = new Store();
		String SQL = "";
		String SQLCronos = "";

		try {
			con = ConnectionDB.lastConnection(dataInstance);
			con.setAutoCommit(false);

			for (String[] listStr : lisRecord) {
				String nameTable = SqlInstruction.getTableFromSql(listStr[4]);
				SQL = "update " + nameTable.trim() + " set " + listStr[3]
						+ "= ?" + " where ROWID='" + listStr[1] + "'";

				// Esto para crear la consulta con los valores para ser guardado
				// en cronos
				Pattern patron = Pattern.compile("(\\?)",
						Pattern.CASE_INSENSITIVE);
				Matcher encaja = patron.matcher(SQL);
				SQLCronos = encaja.replaceAll(listStr[0].trim());

				ps = con.prepareStatement(SQL);
				if (listStr[2].equals("NUMBER")) {
					BigDecimal bid = new BigDecimal(listStr[0].trim());
					ps.setBigDecimal(1, bid);
				} else if (listStr[2].equals("VARCHAR2")) {
					ps.setString(1, listStr[0].trim());
				} else if (listStr[2].equals("DATE")) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
					java.util.Date dat = sdf.parse(listStr[0].trim());
					java.sql.Timestamp sqlFec = new java.sql.Timestamp(
							dat.getTime());

					ps.setTimestamp(1, sqlFec);
				}
				ps.executeUpdate();

				int romAfe = ps.getUpdateCount();
				if (romAfe > 0) {
					responseDto.setSqlBuffer("Succesfully modified records");
				}
				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user") });
				logSql.setDescripcionAudit(SQLCronos + "; " + SQLSelect);
				logSql.setProceso("Class ExecuteSql, Procedure UpdateRecords");
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), "SQL statement executed",
						dataInstance.get("instance") });
				logSql.setCod("AC3");
				store.save("3", logSql);
			}

			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException ex) {
			responseDto.setSqlBuffer(ex.getLocalizedMessage());

			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user") });
			logSql.setDescripcionAudit(responseDto.getSqlBuffer());
			logSql.setProceso("Class ExecuteSql, Procedure UpdateRecords");
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), SQLCronos + ";" + SQLSelect,
					dataInstance.get("instance") });
			logSql.setCod("AC3");
			store.save("3", logSql);

			try {
				con.rollback();
			} catch (Exception e) {
				responseDto.setSqlBuffer(e.getLocalizedMessage());

				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), SQLCronos + ";" + SQLSelect,
						dataInstance.get("instance") });
				logSql.setDescripcionAudit(responseDto.getSqlBuffer());
				logSql.setProceso("Class ExecuteSql, Procedure UpdateRecords");
				logSql.setCod("AC3");
				store.save("1", logSql);
			}
		} catch (Exception e) {
			responseDto.setSqlBuffer(e.getLocalizedMessage());

			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), SQLCronos + ";" + SQLSelect,
					dataInstance.get("instance") });
			logSql.setDescripcionAudit(responseDto.getSqlBuffer());
			logSql.setProceso("Class ExecuteSql, Procedure UpdateRecords");
			logSql.setCod("AC3");
			store.save("1", logSql);

			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (Exception ex) {
				responseDto.setSqlBuffer(ex.getLocalizedMessage());

				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), SQLCronos + ";" + SQLSelect,
						dataInstance.get("instance") });
				logSql.setDescripcionAudit(responseDto.getSqlBuffer());
				logSql.setProceso("Class ExecuteSql, Procedure UpdateRecords");
				logSql.setCod("AC3");
				store.save("1", logSql);
			}
			ps = null;
			try {
				con.close();
			} catch (Exception ex) {
				responseDto.setSqlBuffer(ex.getLocalizedMessage());

				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), SQLCronos + ";" + SQLSelect,
						dataInstance.get("instance") });
				logSql.setDescripcionAudit(responseDto.getSqlBuffer());
				logSql.setProceso("Class ExecuteSql, Procedure UpdateRecords");
				logSql.setCod("AC3");
				store.save("1", logSql);
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
	public String getCLOB(String[] parameters, Map<String, String> dataInstance) {
		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		String result = "";
		LogSql logSql = new LogSql();
		Store store = new Store();
		boolean moreResult;

		try {
			con = ConnectionDB.lastConnection(dataInstance);
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
				strOut.append(aux);
			}
			result = strOut.toString();

		} catch (SQLException ex) {
			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user") });
			logSql.setDescripcionAudit(ex.getLocalizedMessage());
			logSql.setProceso("Class ExecuteSql, Procedure UpdateRecords");
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), "", dataInstance.get("instance") });
			logSql.setCod("AC3");
			store.save("3", logSql);
		} catch (IOException e) {
			e.printStackTrace();
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
			List<BlocksVariable> lisBlo, Map<String, String> dataInstance) {

		String SQL = SqlInstruction.replaceTextBlock(requestDTO.getStringSQL(),
				lisBlo);
		CallableStatement cstmt = null;
		ResponseDTO responseDto = new ResponseDTO();
		ResultSet rs = null;
		Connection con = null;
		LogSql logSql = new LogSql();
		Store store = new Store();

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
			con = ConnectionDB.lastConnection(dataInstance);
			con.setAutoCommit(false);
			responseDto.setSqlBuffer(printAllExceptions(con.getWarnings()));
			con.clearWarnings();

			JdbcUtil.enable_dbms_output(con, ACConstant.SIZE_BUFFER,
					dataInstance);

			cstmt = con.prepareCall(SQL);

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

			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user") });
			logSql.setDescripcionAudit(requestDTO.getStringSQL() + variBloc);
			logSql.setProceso("Class ExecuteSql, Procedure executeSP");
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), "SQL statement executed",
					dataInstance.get("instance") });
			logSql.setCod("AC3");
			store.save("3", logSql);

		} catch (SQLException e) {
			responseDto.setSqlBuffer(e.getLocalizedMessage());

			logSql.setUsuario(dataInstance.get("analyst"));
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user") });
			logSql.setDescripcionAudit(requestDTO.getStringSQL() + variBloc);
			logSql.setProceso("Class ExecuteSql, Procedure UpdateRecords");
			logSql.setCamposTexto(new String[] { dataInstance.get("url"),
					dataInstance.get("user"), responseDto.getSqlBuffer(),
					dataInstance.get("instance") });
			logSql.setCod("AC3");
			store.save("3", logSql);
		} finally {
			try {
				cstmt.close();
			} catch (Exception ex) {
				responseDto.setSqlBuffer(ex.getLocalizedMessage());

				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance") });
				logSql.setDescripcionAudit(requestDTO.getStringSQL() + variBloc);
				logSql.setProceso("Class ExecuteSql, Procedure Execute");
				logSql.setCod("AC3");
				store.save("1", logSql);
			}
			cstmt = null;
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception ex) {
				responseDto.setSqlBuffer(ex.getLocalizedMessage());

				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance") });
				logSql.setDescripcionAudit(requestDTO.getStringSQL() + variBloc);
				logSql.setProceso("Class ExecuteSql, Procedure Execute");
				logSql.setCod("AC3");
				store.save("1", logSql);
			}
			rs = null;
			try {
				con.close();
			} catch (Exception ex) {
				responseDto.setSqlBuffer(ex.getLocalizedMessage());

				logSql.setUsuario(dataInstance.get("analyst"));
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), responseDto.getSqlBuffer(),
						dataInstance.get("instance") });
				logSql.setDescripcionAudit(requestDTO.getStringSQL() + variBloc);
				logSql.setProceso("Class ExecuteSql, Procedure Execute");
				logSql.setCod("AC3");
				store.save("1", logSql);
			}
			con = null;
		}
		return responseDto;
	}
}
