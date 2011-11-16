package com.pratech.accesscontroldb.DTO;

import java.util.List;

/**
 * Informacion enviada a la vista.
 * 
 * @since 2011-07-01
 * @author Diego Alberto Piedrahita
 * @email dpiedrahita@pratechgroup.com
 * @Company PRATECH S.A.S.
 */
@SuppressWarnings("serial")
public class ResponseDTO extends DTO{

	private String sqlBuffer;
	private int totalRows = 0;
	private List<String[]> listData;
	private List<String[]> listBlockSQL;
	private String nameFileExport;
	private String stringSQL;
	private boolean SQLServer;
	private String[] columnRowid;

	public List<String[]> getListData() {
		return listData;
	}

	public void setListData(List<String[]> listData) {
		this.listData = listData;
	}

	public String getSqlBuffer() {
		return sqlBuffer;
	}

	public void setSqlBuffer(String sqlBuffer) {
		this.sqlBuffer = sqlBuffer;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	/**
	 * @return the listBlockSQL
	 */
	public List<String[]> getListBlockSQL() {
		return listBlockSQL;
	}

	/**
	 * @param listBlockSQL the listBlockSQL to set
	 */
	public void setListBlockSQL(List<String[]> listBlockSQL) {
		this.listBlockSQL = listBlockSQL;
	}

	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String toString() {
		return null;
	}

	public String getNameFileExport() {
		return nameFileExport;
	}

	public void setNameFileExport(String nameFileExport) {
		this.nameFileExport = nameFileExport;
	}

	public String getStringSQL() {
		return stringSQL;
	}

	public void setStringSQL(String stringSQL) {
		this.stringSQL = stringSQL;
	}

	public boolean isSQLServer() {
		return SQLServer;
	}

	public void setSQLServer(boolean sQLServer) {
		SQLServer = sQLServer;
	}

	public String[] getColumnRowid() {
		return columnRowid;
	}

	public void setColumnRowid(String[] columnRowid) {
		this.columnRowid = columnRowid;
	}	
}
