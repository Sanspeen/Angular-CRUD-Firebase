package com.pratech.accesscontroldb.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.pratech.accesscontroldb.client.DTO.BlocksVarible;
import com.pratech.accesscontroldb.client.DTO.DataConnection;
import com.pratech.accesscontroldb.client.DTO.RequestDTO;
import com.pratech.accesscontroldb.client.DTO.ResponseDTO;

import java.util.List;

/**
 * Interface methods for asynchronous calls to the server
 * 
 * @since 2011-07-01
 * @author Diego Alberto Piedrahita
 * @email dpiedrahita@pratechgroup.com
 * @Company PRATECH S.A.S.
 * 
 */
@RemoteServiceRelativePath("sqlengineservice")
public interface SqlEngineService extends RemoteService {

	/**
	 * Send information to execute the SQL query
	 * 
	 * @param rq
	 * @param lisBlo
	 * @param dataConnection
	 * @return
	 * @throws ACDBException 
	 */
	public ResponseDTO sendSql(RequestDTO rq, List<BlocksVarible> lisBlo,
			DataConnection dataConnection) throws ACDBException;

	/**
	 * Fill the combo environment
	 * 
	 * @return
	 */
	public List<String> listAmbi();

	/**
	 * Fill the combo instance
	 * 
	 * @param dat
	 *            - Data selected in the previous combo
	 * @return
	 */
	public List<String> listInst(String dat);
	
	/**
	 * Validate XML instances file
	 * 
	 * @return
	 */
	public String validateInstancesXMLFile();

	/**
	 * Gets the data connection from the connection window
	 * 
	 * @param dataConnection
	 * @return
	 * @throws ACDBException 
	 */
	public String[] connectionEntry(DataConnection dataConnection) throws ACDBException;

	/**
	 * identifies the user information from SEUS
	 * 
	 * @return
	 * @throws ACDBException 
	 */
	public String[] isUserAdmin() throws ACDBException;

	/**
	 * Update the changed record in the results table
	 * 
	 * @param listUpdate
	 * @return
	 * @throws ACDBException 
	 */
	public ResponseDTO updateRecords(List<String[]> listUpdate,
			DataConnection dataConnection, String SQLSelect) throws ACDBException;

	/**
	 * Gets as the information from a CLOB field
	 * 
	 * @param parameters
	 * @return
	 * @throws ACDBException 
	 */
	public String getCLOB(String[] parameters, DataConnection dataConnection) throws ACDBException;
	
	String greetServer(String name) throws IllegalArgumentException;
	
	public void deleteFileExcel(String nameFile);
	
	public List<String[]> listComb();
	
	public String getXMLType(String[] parameters, DataConnection dataConnection) throws ACDBException;

	Integer getUserSessionTimeout();
	
	Boolean isSessionAlive();
	
	void ping();	

	String echo(String sqlBuffer);	
	
}
