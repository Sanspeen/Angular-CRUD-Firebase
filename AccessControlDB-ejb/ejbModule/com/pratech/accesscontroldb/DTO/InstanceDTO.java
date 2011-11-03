package com.pratech.accesscontroldb.DTO;

/**
 * 
 * Datos de conexión para acceso a la instancia de base de datos
 * 
 * @since  20110701 
 * @author dpiedrahita
 * @email dpiedrahita@pratechgroup.com
 * @company PRATECH S.A.S.
 * 
 */
@SuppressWarnings("serial")
public class InstanceDTO extends DTO{
	
	/** */ 
	private String url;
	/** */
	private String user;
	/** */
	private String password;
	/** */
	private String analyst;
	/** */
	private String instance;
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the analyst
	 */
	public String getAnalyst() {
		return analyst;
	}
	/**
	 * @param analyst the analyst to set
	 */
	public void setAnalyst(String analyst) {
		this.analyst = analyst;
	}
	/**
	 * @return the instance
	 */
	public String getInstance() {
		return instance;
	}
	/**
	 * @param instance the instance to set
	 */
	public void setInstance(String instance) {
		this.instance = instance;
	}
	
}
