
package com.pratech.accesscontroldb.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface SqlEngineServiceAsync {

	void sendSql(RequestDTO rq, List<BlocksVarible> lisBlo,
			DataConnection dataConnection,
			AsyncCallback<ResponseDTO> asyncCallback);

	public void listInst(String dat, AsyncCallback<List<String>> asyncCallback);

	public void listAmbi(AsyncCallback<List<String>> asyncCallback);

	public void connectionEntry(DataConnection dataConnection,
			AsyncCallback<String[]> asyncCallback);

	void updateRecords(List<String[]> listUpdate,
			DataConnection dataConnection, String SQLSelect,
			AsyncCallback<ResponseDTO> callback);

	void getCLOB(String[] parameters, DataConnection dataConnection,
			AsyncCallback<String> callback);

	void isUserAdmin(AsyncCallback<String[]> callback);
	
}
