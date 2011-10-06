package com.pratech.accesscontroldb.client;

import java.io.Serializable;

/**
 * Information coming from the view
 * 
 * @since 2011-07-01
 * @author Diego Alberto Piedrahita
 * @email dpiedrahita@pratechgroup.com
 * @Company PRATECH S.A.S.
 */
public class RequestDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String stringSQL;
    private int start = 0;
    private int numRow = 0;
    private boolean rowCountFlag = false;
    private int typeSql;
    private String url;
    private String user;
    private String bitacora;
    private String token;

    public RequestDTO() {
    }

    public String getStringSQL() {
        return stringSQL;
    }

    public void setStringSQL(String stringSQL) {
        this.stringSQL = stringSQL;
    }

    public int getTypeSql() {
        return typeSql;
    }

    public void setTypeSql(int typeSql) {
        this.typeSql = typeSql;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBitacora() {
        return bitacora;
    }

    public void setBitacora(String bitacora) {
        this.bitacora = bitacora;
    }

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getNumRow() {
		return numRow;
	}

	public void setNumRow(int numRow) {
		this.numRow = numRow;
	}

	public boolean isRowCountFlag() {
		return rowCountFlag;
	}

	public void setRowCountFlag(boolean rowCountFlag) {
		this.rowCountFlag = rowCountFlag;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
}
