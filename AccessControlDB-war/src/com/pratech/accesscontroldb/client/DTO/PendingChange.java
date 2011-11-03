package com.pratech.accesscontroldb.client.DTO;

public abstract class PendingChange<T> {
	public final RecordsTable recordsTable;
	public final T value;
	public final Integer id;

	public PendingChange(RecordsTable recordsTable, T value, Integer id) {
		this.recordsTable = recordsTable;
		this.value = value;
		this.id = id;
	}
	
	protected abstract void doCommit(RecordsTable recordsTable, T value,
			Integer id);
}
