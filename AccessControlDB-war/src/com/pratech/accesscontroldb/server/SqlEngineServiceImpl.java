package com.pratech.accesscontroldb.server;

import co.com.suramericana.seus.ws.interfaces.SeusWs_SeusWsImplPort_Client;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pratech.accesscontroldb.client.ACDBException;
import com.pratech.accesscontroldb.client.SqlEngineService;
import com.pratech.accesscontroldb.client.DTO.BlocksVarible;
import com.pratech.accesscontroldb.client.DTO.DataConnection;
import com.pratech.accesscontroldb.client.DTO.RequestDTO;
import com.pratech.accesscontroldb.client.DTO.ResponseDTO;
import com.pratech.accesscontroldb.common.Configuration;
import com.pratech.accesscontroldb.core.SqlEngineSB;
import com.pratech.accesscontroldb.persistence.Store;

import java.util.ArrayList;
import java.util.List;

public class SqlEngineServiceImpl extends RemoteServiceServlet implements
		SqlEngineService {

	private static final long serialVersionUID = 1L;
	private int timeout;

	public ResponseDTO sendSql(RequestDTO rq, List<BlocksVarible> listBlock,
			DataConnection dataConnection) throws ACDBException {
		ResponseDTO response = new ResponseDTO();
		com.pratech.accesscontroldb.DTO.RequestDTO requestRemote = new com.pratech.accesscontroldb.DTO.RequestDTO();

		// Request
		com.pratech.accesscontroldb.DTO.ResponseDTO responsetRemote = new com.pratech.accesscontroldb.DTO.ResponseDTO();
		com.pratech.accesscontroldb.DTO.DataConnection dConneRemote = new com.pratech.accesscontroldb.DTO.DataConnection();

		List<com.pratech.accesscontroldb.DTO.BlocksVariable> lisBloRemote = new ArrayList<com.pratech.accesscontroldb.DTO.BlocksVariable>(
				0);

		requestRemote.setStringSQL(rq.getStringSQL());
		requestRemote.setBitacora(rq.getBitacora());
		requestRemote.setTypeSql(rq.getTypeSql());
		requestRemote.setUrl(rq.getUrl());
		requestRemote.setUser(rq.getUser());
		requestRemote.setNumRow(rq.getNumRow());
		requestRemote.setRowCountFlag(rq.isRowCountFlag());
		requestRemote.setStart(rq.getStart());
		requestRemote.setBlockSQL(rq.getBlockSQL());
		requestRemote.setCommitBlock(rq.isCommitBlock());
		requestRemote.setExportData(rq.getExportData());
		requestRemote.setSQLServer(rq.isSQLServer());
		//Modificado el 2012-05-23 por Juan
		requestRemote.setExplainPlan(rq.isExplainPlan());

		dConneRemote = passDataConnection(dataConnection);

		for (BlocksVarible block : listBlock) {
			com.pratech.accesscontroldb.DTO.BlocksVariable blockRemote = new com.pratech.accesscontroldb.DTO.BlocksVariable();
			blockRemote.setAddress(block.getAddress());
			blockRemote.setTypeData(block.getTypeData());
			blockRemote.setUse(block.isUse());
			blockRemote.setValue(block.getValue());
			blockRemote.setVariable(block.getVariable());
			lisBloRemote.add(blockRemote);
		}

		SqlEngineSB sqlEngineSB = new SqlEngineSB();
		responsetRemote = sqlEngineSB.doSql(requestRemote, lisBloRemote,
				dConneRemote);

		response.setListData(responsetRemote.getListData());
		response.setSqlBuffer(responsetRemote.getSqlBuffer());
		response.setTotalRows(responsetRemote.getTotalRows());
		response.setListBlockSQL(responsetRemote.getListBlockSQL());
		response.setNameFileExport(responsetRemote.getNameFileExport());
		response.setStringSQL(responsetRemote.getStringSQL());
		response.setSQLServer(responsetRemote.isSQLServer());
		response.setColumnRowid(responsetRemote.getColumnRowid());

		return response;
	}

	public List<String> listAmbi() {
		List<String> lisAmb = new ArrayList<String>(0);
		SqlEngineSB sqlEngineSB = new SqlEngineSB();
		lisAmb = sqlEngineSB.listAmbi();
		return lisAmb;
	}

	public List<String> listInst(String dat) {
		List<String> lisInst = new ArrayList<String>(0);
		SqlEngineSB sqlEngineSB = new SqlEngineSB();
		lisInst = sqlEngineSB.listInst(dat);
		return lisInst;
	}

	public String validateInstancesXMLFile() {
		String validation;
		SqlEngineSB sqlEngineSB = new SqlEngineSB();
		validation = sqlEngineSB.validateInstancesXMLFile();
		return validation;
	}
	
	public List<String[]> listComb() {
		SqlEngineSB sqlEngineSB = new SqlEngineSB();
		return sqlEngineSB.listComb();

	}

	public String[] connectionEntry(DataConnection dataConnection) throws ACDBException {
		com.pratech.accesscontroldb.DTO.DataConnection dConneRemote = new com.pratech.accesscontroldb.DTO.DataConnection();

		dConneRemote = passDataConnection(dataConnection);

		SqlEngineSB sqlEngineSB = new SqlEngineSB();
		return sqlEngineSB.connectionEntry(dConneRemote);
	}

	public ResponseDTO updateRecords(List<String[]> listUpdate,
			DataConnection dataConnection, String SQLSelect) throws ACDBException {
		ResponseDTO response = new ResponseDTO();
		com.pratech.accesscontroldb.DTO.ResponseDTO responsetRemote = new com.pratech.accesscontroldb.DTO.ResponseDTO();
		com.pratech.accesscontroldb.DTO.DataConnection dConneRemote = new com.pratech.accesscontroldb.DTO.DataConnection();

		dConneRemote = passDataConnection(dataConnection);

		SqlEngineSB sqlEngineSB = new SqlEngineSB();
		responsetRemote = sqlEngineSB.updateRecords(listUpdate, dConneRemote,
				SQLSelect);
		response.setListData(responsetRemote.getListData());
		response.setSqlBuffer(responsetRemote.getSqlBuffer());
		response.setTotalRows(responsetRemote.getTotalRows());
		return response;
	}

	public String getCLOB(String[] parameters, DataConnection dataConnection) throws ACDBException {
		com.pratech.accesscontroldb.DTO.DataConnection dConneRemote = new com.pratech.accesscontroldb.DTO.DataConnection();

		dConneRemote = passDataConnection(dataConnection);

		SqlEngineSB sqlEngineSB = new SqlEngineSB();
		return sqlEngineSB.getCLOB(parameters, dConneRemote);
	}

	public String getXMLType(String[] parameters, DataConnection dataConnection) throws ACDBException {
		com.pratech.accesscontroldb.DTO.DataConnection dConneRemote = new com.pratech.accesscontroldb.DTO.DataConnection();

		dConneRemote = passDataConnection(dataConnection);

		SqlEngineSB sqlEngineSB = new SqlEngineSB();
		return sqlEngineSB.getXMLType(parameters, dConneRemote);
	}

	public String[] isUserAdmin() throws ACDBException {

		try {
		String urlReferer = getThreadLocalRequest().getHeader("referer");

		String token = urlReferer.toLowerCase().replaceAll("^.*\\?id=", "")
				.replaceAll("&.*", "");

		SeusWs_SeusWsImplPort_Client portClient = new SeusWs_SeusWsImplPort_Client(
				Configuration.getInstance().getProperty("SERVICE_NAME"),
				"SeusWsImplService");

		co.com.suramericana.seus.ws.bo.UsuarioSeus userSeus = portClient
				.getUser(token);

		return new String[] { userSeus.getName().getValue(),
				userSeus.getLogin().getValue() };
		} catch (RuntimeException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error al obtener información de usuario", e);
			throw e;
		}
	}

	public String greetServer(String input) throws IllegalArgumentException {
		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script
		// vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	public void deleteFileExcel(String nameFile) {
		SqlEngineSB sqlEngineSB = new SqlEngineSB();
		sqlEngineSB.deleteFileExcel(nameFile);
	}

	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	private com.pratech.accesscontroldb.DTO.DataConnection passDataConnection(
			DataConnection dataConnection) {
		com.pratech.accesscontroldb.DTO.DataConnection dConneRemote = new com.pratech.accesscontroldb.DTO.DataConnection();
		dConneRemote.setAnalyst(dataConnection.getAnalyst());
		dConneRemote.setApplication(dataConnection.getApplication());
		dConneRemote.setCompany(dataConnection.getCompany());
		dConneRemote.setInstance(dataConnection.getInstance());
		dConneRemote.setPassword(dataConnection.getPassword());
		dConneRemote.setScope(dataConnection.getScope());
		dConneRemote.setSolution(dataConnection.getSolution());
		dConneRemote.setSource(dataConnection.getSource());
		dConneRemote.setSourceNumber(dataConnection.getSourceNumber());
		dConneRemote.setUser(dataConnection.getUser());
		dConneRemote.setUrl(dataConnection.getUrl());
		dConneRemote.setIpUser(getThreadLocalRequest().getRemoteAddr());
		dConneRemote.setTeam(dataConnection.getTeam());
		dConneRemote.setObservations(dataConnection.getObservations());
		dConneRemote.setTransaction(dataConnection.getTransaction());
		return dConneRemote;
	}

	public Integer getUserSessionTimeout() {
		timeout = getThreadLocalRequest().getSession().getMaxInactiveInterval() * 1000;
		return timeout;
	}

	public Boolean isSessionAlive() {
		int tim = (int) System.currentTimeMillis();
		int rr = (int) getThreadLocalRequest().getSession().getLastAccessedTime();
		System.out.println(tim - rr + " < " + timeout);
		return new Boolean(
				(System.currentTimeMillis() - getThreadLocalRequest()
						.getSession().getLastAccessedTime()) < timeout);
	}

	public void ping() {
	}

	//Se utiliza para mostrar los resultados del explain plan
	public String echo(String s) {
		return s;
	}

}
