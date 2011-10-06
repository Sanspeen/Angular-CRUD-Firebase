package com.pratech.accesscontroldb.client;

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
}
