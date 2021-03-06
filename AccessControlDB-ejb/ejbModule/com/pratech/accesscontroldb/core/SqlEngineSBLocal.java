package com.pratech.accesscontroldb.core;

import com.pratech.accesscontroldb.DTO.BlocksVariable;
import com.pratech.accesscontroldb.DTO.DataConnection;
import com.pratech.accesscontroldb.DTO.RequestDTO;
import com.pratech.accesscontroldb.DTO.ResponseDTO;
import com.pratech.accesscontroldb.client.ACDBException;

import java.util.List;

import javax.ejb.Local;

/**
 * Esta clase est? a la espera de ser llamado desde GWT
 * 
 * @since 2011-07-01
 * @author Diego Alberto Piedrahita
 * @email dpiedrahita@pratechgroup.com
 * @Company PRATECH S.A.S.
 * 
 */
@Local
public interface SqlEngineSBLocal {

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
	 * @throws ACDBException 
	 */
	public ResponseDTO doSql(RequestDTO requestDto,
			List<BlocksVariable> lisBlo, DataConnection dataConnection) throws ACDBException;

	/**
	 * Lista la informacion de los ambientes
	 * 
	 * @return = Lista de ambientes
	 */

	public List<String> listAmbi();

	/**
	 * Lista la informacion de las instancias
	 * 
	 * @param dat
	 *            = codigo del ambiente
	 * @return = Lista de instancias
	 * 
	 */
	public List<String> listInst(String dat);

	/**
	 * Valida y retorna los datos ingresados en la ventana de conexion. Solo los
	 * referentes a la conexion a base de datos
	 * 
	 * @param dataConnection
	 *            = datos requeridos para realziar la conexion
	 * 
	 * @return
	 * @throws ACDBException 
	 * 
	 * @see com.pratech.accesscontroldb.core.SqlEngineSBLocal#connectionEntry(com
	 *      .pratech.accesscontroldb.DTO.DataConnection)
	 */
	public String[] connectionEntry(DataConnection dataConnection) throws ACDBException;

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
			DataConnection dataConnection, String SQLSelect) throws ACDBException;

	/**
	 * Retorna la informacion de los tipos de datos CLOB
	 * 
	 * @param parameters
	 *            = vector con los datos para obtener el datos CLOB. Vector
	 *            positions 0 - Nombre del campo 1 - Numero de registro 2 -
	 *            Instruccion SQL
	 * @throws ACDBException 
	 */
	public String getCLOB(String[] parameters, DataConnection dataConnection) throws ACDBException;

	/**
	 * Elimina archivo de excel temporal
	 * 
	 * @param nameFile
	 *            = Nombre del archivo a aliminar
	 */
	public void deleteFileExcel(String nameFile);

	public List<String[]> listComb();

	public String getXMLType(String[] parameters, DataConnection dataConnection) throws ACDBException;
	
	public String updateInstancesXML();
	
}
