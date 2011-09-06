package com.pratech.accesscontroldb.server;

//import co.com.suramericana.seus.ws.bo.UsuarioSeus;
//import co.com.suramericana.seus.ws.interfaces.SeusWsProxy;

import co.com.suramericana.seus.ws.interfaces.SeusWs_SeusWsImplPort_Client;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pratech.accesscontroldb.client.BlocksVarible;
import com.pratech.accesscontroldb.client.DataConnection;
import com.pratech.accesscontroldb.client.RequestDTO;
import com.pratech.accesscontroldb.client.ResponseDTO;

import com.pratech.accesscontroldb.client.SqlEngineService;
import com.pratech.accesscontroldb.common.Configuration;
import com.pratech.accesscontroldb.core.SqlEngineSB;

import javax.xml.namespace.QName;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class SqlEngineServiceImpl extends RemoteServiceServlet implements
		SqlEngineService {

	public ResponseDTO sendSql(RequestDTO rq, List<BlocksVarible> listBlock,
			DataConnection dataConnection) {
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

	public String[] connectionEntry(DataConnection dataConnection) {
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
		
		SqlEngineSB sqlEngineSB = new SqlEngineSB();
		return sqlEngineSB.connectionEntry(dConneRemote);
	}

	public ResponseDTO updateRecords(List<String[]> listUpdate,
			DataConnection dataConnection, String SQLSelect) {
		ResponseDTO response = new ResponseDTO();
		com.pratech.accesscontroldb.DTO.ResponseDTO responsetRemote = new com.pratech.accesscontroldb.DTO.ResponseDTO();
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

		SqlEngineSB sqlEngineSB = new SqlEngineSB();
		responsetRemote = sqlEngineSB.updateRecords(listUpdate, dConneRemote, SQLSelect);
		response.setListData(responsetRemote.getListData());
		response.setSqlBuffer(responsetRemote.getSqlBuffer());
		response.setTotalRows(responsetRemote.getTotalRows());
		return response;
	}

	public String getCLOB(String[] parameters, DataConnection dataConnection) {
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
		
		SqlEngineSB sqlEngineSB = new SqlEngineSB();
		return sqlEngineSB.getCLOB(parameters, dConneRemote);
	}

	public String[] isUserAdmin() {

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
	}

}
