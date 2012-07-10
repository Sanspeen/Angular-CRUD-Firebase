package com.pratech.accesscontroldb.client.DTO;

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
    private String[] blockSQL;
    private boolean commitBlock;
    private int exportData;
    private boolean SQLServer;
    private String[] columnRowid;
    //Modificado el 2012-05-23 por Juan
  	private boolean explainPlan;

  	public boolean isExplainPlan() {
  		return explainPlan;
  	}

  	public void setExplainPlan(boolean explainPlan) {
  		this.explainPlan = explainPlan;
  	}

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String[] getBlockSQL() {
		return blockSQL;
	}

	public void setBlockSQL(String[] blockSQL) {
		this.blockSQL = blockSQL;
	}

	public boolean isCommitBlock() {
		return commitBlock;
	}

	public void setCommitBlock(boolean commitBlock) {
		this.commitBlock = commitBlock;
	}

	public int getExportData() {
		return exportData;
	}

	public void setExportData(int exportData) {
		this.exportData = exportData;
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
