package com.pratech.accesscontroldb.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pratech.accesscontroldb.client.DTO.BlocksVarible;
import com.pratech.accesscontroldb.client.DTO.DataConnection;
import com.pratech.accesscontroldb.client.DTO.RequestDTO;
import com.pratech.accesscontroldb.client.DTO.ResponseDTO;

import java.util.List;

public interface SqlEngineServiceAsync {

	void sendSql(RequestDTO rq, List<BlocksVarible> lisBlo,
			DataConnection dataConnection,
			AsyncCallback<ResponseDTO> asyncCallback);

	public void listInst(String dat, AsyncCallback<List<String>> asyncCallback);

	public void listAmbi(AsyncCallback<List<String>> asyncCallback);

	void updateInstancesXMLFile(AsyncCallback<String> callback);
	
	void validateInstancesXMLFile(AsyncCallback<String> callback);
	
	void connectionEntry(DataConnection dataConnection,
			AsyncCallback<String[]> asyncCallback);

	void updateRecords(List<String[]> listUpdate,
			DataConnection dataConnection, String SQLSelect,
			AsyncCallback<ResponseDTO> callback);

	void getCLOB(String[] parameters, DataConnection dataConnection,
			AsyncCallback<String> callback);

	void isUserAdmin(AsyncCallback<String[]> callback);

	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void deleteFileExcel(String nameFile, AsyncCallback<Void> callback);

	void listComb(AsyncCallback<List<String[]>> callback);

	void getXMLType(String[] parameters, DataConnection dataConnection,
			AsyncCallback<String> callback);
	
	void getUserSessionTimeout(AsyncCallback<Integer> callback);
	
	void isSessionAlive(AsyncCallback<Boolean> callback);

	void ping(AsyncCallback callback);


}
