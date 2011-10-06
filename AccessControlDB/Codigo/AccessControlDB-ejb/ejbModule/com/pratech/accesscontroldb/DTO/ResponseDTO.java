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

	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String toString() {
		return null;
	}

}
