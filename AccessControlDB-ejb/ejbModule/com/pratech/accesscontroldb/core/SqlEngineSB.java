package com.pratech.accesscontroldb.core;

import com.pratech.accesscontroldb.DTO.BlocksVariable;
import com.pratech.accesscontroldb.DTO.DataConnection;
import com.pratech.accesscontroldb.DTO.LogSql;
import com.pratech.accesscontroldb.DTO.RequestDTO;
import com.pratech.accesscontroldb.DTO.ResponseDTO;
import com.pratech.accesscontroldb.client.ACDBException;
import com.pratech.accesscontroldb.common.ACConfig;
import com.pratech.accesscontroldb.core.ad.ExecuteSql;
import com.pratech.accesscontroldb.core.ad.IdentifyClientIdSession;
import com.pratech.accesscontroldb.core.connection.ConnectionDB;
import com.pratech.accesscontroldb.persistence.DeleteFile;
import com.pratech.accesscontroldb.persistence.Store;
import com.pratech.accesscontroldb.persistence.XMLData;
import com.pratech.accesscontroldb.util.SqlInstruction;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateful;

import suramericana.cronos.utilidades.GeneradorCodigos;

/**
 * Esta clase está a la espera de ser llamado desde GWT.
 * 
 * @since 2011-07-01
 * @author Diego Alberto Piedrahita
 * @email dpiedrahita@pratechgroup.com
 * @Company PRATECH S.A.S.
 * 
 */
@Stateful
public class SqlEngineSB implements SqlEngineSBLocal {

	private Map<String, String> dataInstance = new HashMap<String, String>();

	/**
	 * Recibe las intrucciones SQL y las convierte y enruta deacuerdo al tipo de
	 * instruccion
	 * 
	 * @param requestDto
	 *            = DTO Con los datos de la vista
	 * @param dataConnection
	 *            = Datos de la conexion.
	 * @param lisBlo
	 *            = lista con las variables utilizadas en los bloques anonimos
	 * 
	 * @return = DTO con la informacion a retornar.
	 */
	public ResponseDTO doSql(RequestDTO requestDto,
			List<BlocksVariable> lisBlo, DataConnection dataConnection) throws ACDBException {

		setDataInstance(dataConnection);

		// The object to return
		ResponseDTO responseDTO = new ResponseDTO();
		String SQL = requestDto.getStringSQL();
		// String sql clean
		
		//Modificado el 2012-05-23 por Juan
		if(requestDto.isExplainPlan()){
			requestDto.setTypeSql(3);		
		}
		else if (!requestDto.isCommitBlock()) {
			SqlInstruction formatterSql = new SqlInstruction();
			requestDto = formatterSql.editSql(requestDto, dataInstance);
		} else {
			requestDto.setTypeSql(2);
		}

		if (requestDto.getStringSQL().length() > 0) {
			ExecuteSql executeSql = new ExecuteSql();
			if (requestDto.getTypeSql() == 1) {
				if (lisBlo.get(0).getAddress() == null) {
					requestDto.setTypeSql(0);
				}
			}
			switch (requestDto.getTypeSql()) {
			case 0:
				responseDTO = executeSql.executePaginacion(requestDto,
						dataInstance);
				break;
			case 1:
				responseDTO = executeSql.executeSP(requestDto, lisBlo,
						dataInstance);
				break;
			case 2:
				responseDTO = executeSql.executeBlock(requestDto, dataInstance);
			//Modificado el 2012-05-23 por Juan
				break;
			case 3:
				responseDTO=executeSql.executeExplainPlan(requestDto, dataInstance);
				break;
			}
		}
		/**
		 * Esto para que le retorne a lavista la consulta ejecutada y cuando se
		 * exporte a excel se exporte con base a la consulta ejecutada
		 */
		responseDTO.setColumnRowid(requestDto.getColumnRowid());
		responseDTO.setStringSQL(SQL);
		return responseDTO;
	}

	/**
	 * Lista la informacion de los ambientes
	 * 
	 * @return = Lista de ambientes
	 */
	public List<String> listAmbi() {
		XMLData listInstances = new XMLData();
		List<String> lisAm = new ArrayList<String>();
		lisAm = listInstances.getXMLAmbientes();
		return lisAm;
	}

	/**
	 * Lista la informacion de las instancias
	 * 
	 * @param env
	 *            = codigo del ambiente
	 * @return = Lista de instancias
	 * 
	 */
	public List<String> listInst(String env) {
		XMLData listInstances = new XMLData();
		List<String> lisInst = new ArrayList<String>();
		lisInst = listInstances.readXMLInstances(env);
		return lisInst;
	}

	/**
	 * Valida y retorna los datos ingresados en la ventana de conexion. Solo los
	 * referentes a la conexion a base de datos
	 * 
	 * @param dataConnection
	 *            = datos requeridos para realziar la conexion
	 * 
	 * @return
	 * 
	 * @see com.pratech.accesscontroldb.core.SqlEngineSBLocal#connectionEntry(com
	 *      .pratech.accesscontroldb.DTO.DataConnection)
	 */
	public String[] connectionEntry(DataConnection dataConnection) throws ACDBException {

		XMLData xmlData = new XMLData();
		long transaccion = 0;
		setDataInstance(dataConnection);
		String url = xmlData.readURLXML(dataInstance.get("scope").trim(), dataInstance.get("instance").trim());

		dataInstance.put("url",url);

		if (url.length() > 0) {
			Connection cn = ConnectionDB.createConnection(dataInstance);
			if (url.indexOf("sqlserver") < 0) {
				IdentifyClientIdSession.identifyClientIdSession(cn,
						dataInstance);
			}
			if (cn != null) {
				LogSql logSql = new LogSql();
				transaccion = GeneradorCodigos.generarIdTransaccion();
				//El campo transaccion debe manejarse con una longitud máxima de 15 caracteres
				String strTransaccion = Long.toString(transaccion);
				if (strTransaccion.length() > 15) {
					strTransaccion.substring(strTransaccion.length() - 15, strTransaccion.length());
					transaccion = Long.parseLong(strTransaccion);
				}
				logSql.setTransaccion(transaccion);
				logSql.setDescripcionAudit("Logeo de conexion");
				logSql.setUsuario(dataConnection.getAnalyst());
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), dataInstance.get("source"),
						dataInstance.get("instance"),
						dataInstance.get("scope"),
						dataInstance.get("sourceNumber"),
						dataInstance.get("solution"),
						dataInstance.get("application"),
						dataInstance.get("team") != null && dataInstance.get("team").length() > 255? 
								dataInstance.get("team").substring(0, 255)
								: dataInstance.get("team"), //longitud máxima de team es 255
						dataInstance.get("observations") != null && dataInstance.get("observations").length() > 255? 
								dataInstance.get("observations").substring(0, 255)
								: dataInstance.get("observations") //longitud máxima de observations es 255
				});
				logSql.setCod("AC1");
				Store.getInstance().save("1", logSql);
			}
			try {
				cn.close();
			} catch (Exception e) {
				//Registrar excepción
				e.printStackTrace();
				Store.getInstance().error(dataInstance.get("user"), "Error al cerrar conexión", e);
			}
			cn = null;
		}
		String isSQLServer = "1";
		if (url.toUpperCase().indexOf("SQLSERVER") < 0) {
			isSQLServer = "0";
		}
		return new String[] { url, ACConfig.getValue("numRows"), isSQLServer, transaccion + "" };
	}

	/**
	 * Actualiza los registros modificados en la tabla de resultados
	 * 
	 * @param listUpdate
	 *            = Lista con la informacion de los registros modificados
	 * @param dataConnection
	 *            = Datos de la conexion.
	 * @param SQLSelect
	 *            = Instruccion SQL ingresada.
	 * 
	 * @return DTO con la informacion a retornar.
	 * @throws ACDBException 
	 * 
	 * @see com.pratech.accesscontroldb.core.SqlEngineSBLocal#updateRecords(java.util.List,
	 *      com.pratech.accesscontroldb.DTO.DataConnection, java.lang.String)
	 */
	public ResponseDTO updateRecords(List<String[]> listUpdate,
			DataConnection dataConnection, String SQLSelect) throws ACDBException {
		setDataInstance(dataConnection);
		ResponseDTO responseDTO = new ResponseDTO();
		ExecuteSql executeSql = new ExecuteSql();
		responseDTO = executeSql.UpdateRecords(listUpdate, dataInstance,
				SQLSelect);
		return responseDTO;
	}

	/**
	 * Retorna la informacion de los tipos de datos CLOB
	 * 
	 * @param parameters
	 *            = vector con los datos para obtener el datos CLOB vector
	 *            positions 0 - Field name 1 - New data 2 - Table name
	 */
	public String getCLOB(String[] parameters, DataConnection dataConnection) throws ACDBException {
		setDataInstance(dataConnection);
		String commandTerminator = ACConfig.getValue("commandTerminator");
		if (parameters[2].endsWith(commandTerminator)) {
			parameters[2] = parameters[2].substring(0, parameters[2].length()
					- commandTerminator.length());
		}

		ExecuteSql executeSql = new ExecuteSql();
		return executeSql.getCLOB(parameters, dataInstance);
	}

	public Map<String, String> getDataInstance() {
		return dataInstance;
	}

	/**
	 * Pasa del objeto DataConnection a un HasMap
	 * 
	 * @param dataConnection
	 */
	private void setDataInstance(DataConnection dataConnection) {
		dataInstance.put("user", dataConnection.getUser());
		dataInstance.put("password", dataConnection.getPassword());
		dataInstance.put("analyst", dataConnection.getAnalyst());
		dataInstance.put("instance", dataConnection.getInstance());
		dataInstance.put("source", dataConnection.getSource());
		dataInstance.put("sourceNumber", dataConnection.getSourceNumber());
		dataInstance.put("solution", dataConnection.getSolution());
		dataInstance.put("application", dataConnection.getApplication());
		dataInstance.put("scope", dataConnection.getScope());
		dataInstance.put("url", dataConnection.getUrl());
		dataInstance.put("ip", dataConnection.getIpUser());
		dataInstance.put("team", dataConnection.getTeam());
		dataInstance.put("observations", dataConnection.getObservations());
		dataInstance.put("transaction", dataConnection.getTransaction());
	}

	public void deleteFileExcel(String nameFile) {
		DeleteFile delete = new DeleteFile();
		delete.deleteFileExcel(nameFile);
	}

	public List<String[]> listComb() {
		XMLData xmlData = new XMLData();
		return xmlData.readXML();
	}

	public String getXMLType(String[] parameters, DataConnection dataConnection) throws ACDBException {
		setDataInstance(dataConnection);
		String commandTerminator = ACConfig.getValue("commandTerminator");
		if (parameters[2].endsWith(commandTerminator)) {
			parameters[2] = parameters[2].substring(0, parameters[2].length()
					- commandTerminator.length());
		}
		ExecuteSql executeSql = new ExecuteSql();
		return executeSql.getXMLType(parameters, dataInstance);
	}

	public String validateInstancesXMLFile() {
		XMLData xmlData = new XMLData();
		return xmlData.validateXMLInstances(null);
	}

	public String updateInstancesXML() {
		XMLData xmlData = new XMLData();
		String message = xmlData.updateInstancesXML();
		return message;
	}
	
}
