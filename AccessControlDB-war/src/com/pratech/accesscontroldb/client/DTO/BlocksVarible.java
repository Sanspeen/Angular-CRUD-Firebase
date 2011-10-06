package com.pratech.accesscontroldb.client.DTO;

import java.io.Serializable;

/**
 * Anonymous variable data blocks
 *         
 * @since 2011-07-01
 * @author Diego Alberto Piedrahita
 * @email dpiedrahita@pratechgroup.com
 * @Company PRATECH S.A.S.
 * 
 */

public class BlocksVarible implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BlocksVarible() {}

	public BlocksVarible(boolean use, String variable, String typeData,
			String value) {
		this.use = use;
		this.variable = variable;
		this.typeData = typeData;
		this.value = value;
	}

	private boolean use;
	private String variable;
	private String typeData;
	private String value;
	private String address;

	/**
	 * @return the use
	 */
	public boolean isUse() {
		return use;
	}

	/**
	 * @param use the use to set
	 */
	public void setUse(boolean use) {
		this.use = use;
	}

	/**
	 * @return the variable
	 */
	public String getVariable() {
		return variable;
	}

	/**
	 * @param variable the variable to set
	 */
	public void setVariable(String variable) {
		this.variable = variable;
	}

	/**
	 * @return the typeData
	 */
	public String getTypeData() {
		return typeData;
	}

	/**
	 * @param typeData the typeData to set
	 */
	public void setTypeData(String typeData) {
		this.typeData = typeData;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

}
