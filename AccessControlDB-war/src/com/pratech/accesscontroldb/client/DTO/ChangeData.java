package com.pratech.accesscontroldb.client.DTO;

public class ChangeData extends PendingChange<String>{

	public ChangeData(RecordsTable recordsTable, String value, Integer id) {
		super(recordsTable, value, id);
	}

	@Override
	protected void doCommit(RecordsTable recordsTable, String value, Integer id) {
		recordsTable.reg[id] = value;
		
	}

}
