package com.pratech.accesscontroldb.client.DTO;

import java.io.Serializable;
import java.util.List;

/**
 * Information sent in sight
 * 
 * @since 2011-07-01
 * @author Diego Alberto Piedrahita
 * @email dpiedrahita@pratechgroup.com
 * @Company PRATECH S.A.S.
 */
public class ResponseDTO implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	private String sqlBuffer;
	private int totalRows = 0;
	private List<String[]> listData;
	private List<String[]> listBlockSQL;	
	private String nameFileExport;
	private String stringSQL;
	private boolean SQLServer;
	private String[] columnRowid;

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

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
