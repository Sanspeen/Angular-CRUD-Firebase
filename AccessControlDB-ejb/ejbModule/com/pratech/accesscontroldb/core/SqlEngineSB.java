package com.pratech.accesscontroldb.core;

import com.pratech.accesscontroldb.DTO.BlocksVariable;
import com.pratech.accesscontroldb.DTO.DataConnection;
import com.pratech.accesscontroldb.DTO.LogSql;
import com.pratech.accesscontroldb.DTO.RequestDTO;
import com.pratech.accesscontroldb.DTO.ResponseDTO;
import com.pratech.accesscontroldb.common.ACConfig;
import com.pratech.accesscontroldb.core.ad.ExecuteSql;
import com.pratech.accesscontroldb.core.ad.IdentifyClientIdSession;
import com.pratech.accesscontroldb.core.ad.urlConnection;
import com.pratech.accesscontroldb.core.connection.ConnectionDB;
import com.pratech.accesscontroldb.persistence.DeleteFile;
import com.pratech.accesscontroldb.persistence.Store;
import com.pratech.accesscontroldb.persistence.XMLData;
import com.pratech.accesscontroldb.util.InfoSession;
import com.pratech.accesscontroldb.util.SqlInstruction;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateful;

/**
 * Esta clase está a la espera de ser llamado desde GWT
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
			List<BlocksVariable> lisBlo, DataConnection dataConnection) {

		// The object to return
		ResponseDTO responseDTO = new ResponseDTO();
		String SQL = requestDto.getStringSQL();
		// String sql clean
		if (!requestDto.isCommitBlock()) {
			SqlInstruction formatterSql = new SqlInstruction();
			requestDto = formatterSql.editSql(requestDto);
		} else {
			requestDto.setTypeSql(2);
		}

		setDataInstance(dataConnection);		

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
			}
		}
		/**
		 * Esto para que le retorne a lavista la consulta ejecutada
		 * y cuando se exporte a excel se exporte con base a la consulta ejecutada
		 */
		responseDTO.setStringSQL(SQL);
		return responseDTO;
	}

	/**
	 * Lista la informacion de los ambientes
	 * 
	 * @return = Lista de ambientes
	 */
	public List<String> listAmbi() {
		InfoSession listInstances = new InfoSession();
		List<String> lisAm = new ArrayList<String>();
		lisAm = listInstances.getAmbiente();
		return lisAm;
	}

	/**
	 * Lista la informacion de las instancias
	 * 
	 * @param dat
	 *            = codigo del ambiente
	 * @return = Lista de instancias
	 * 
	 */
	public List<String> listInst(String dat) {
		InfoSession listInstances = new InfoSession();
		List<String> lisInst = new ArrayList<String>();
		lisInst = listInstances.getInstancias(dat);
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
	public String[] connectionEntry(DataConnection dataConnection) {

		setDataInstance(dataConnection);			

		urlConnection urlConnection = new urlConnection();
		String url = urlConnection.URLSearch(dataInstance);
		dataInstance.put("url", url);

		if (url.length() > 0) {
			Store store = new Store();
			Connection cn = ConnectionDB.createConnection(dataInstance);
			IdentifyClientIdSession.identifyClientIdSession(cn, dataInstance);
			if (cn != null) {
				LogSql logSql = new LogSql();
				logSql.setDescripcionAudit("Logeo de conexion");
				logSql.setUsuario(dataConnection.getAnalyst());
				logSql.setCamposTexto(new String[] { dataInstance.get("url"),
						dataInstance.get("user"), dataInstance.get("source"),
						dataInstance.get("sourceNumber"),
						dataInstance.get("solution"),
						dataInstance.get("application"),
						dataInstance.get("instance") });
				logSql.setCod("AC1");
				store.save("1", logSql);
			}
			try {
				cn.close();
			} catch (Exception ex) {
				System.out.println(ex.getLocalizedMessage());
			}
			cn = null;
		}
		return new String[] { url, ACConfig.getValue("numRows") };
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
	 * 
	 * @see com.pratech.accesscontroldb.core.SqlEngineSBLocal#updateRecords(java.util.List,
	 *      com.pratech.accesscontroldb.DTO.DataConnection, java.lang.String)
	 */
	public ResponseDTO updateRecords(List<String[]> listUpdate,
			DataConnection dataConnection, String SQLSelect) {
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
	public String getCLOB(String[] parameters, DataConnection dataConnection) {
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
	}

	public void deleteFileExcel(String nameFile) {
		DeleteFile delete = new DeleteFile();
		delete.deleteFileExcel(nameFile);
	}

	public List<String[]> listComb() {
		XMLData xmlData = new XMLData();
		return xmlData.readXML();
	}

	public String getXMLType(String[] parameters, DataConnection dataConnection) {
		setDataInstance(dataConnection);
		String commandTerminator = ACConfig.getValue("commandTerminator");
		if (parameters[2].endsWith(commandTerminator)) {
			parameters[2] = parameters[2].substring(0, parameters[2].length()
					- commandTerminator.length());
		}
		ExecuteSql executeSql = new ExecuteSql();
		return executeSql.getXMLType(parameters, dataInstance);
	}
}
