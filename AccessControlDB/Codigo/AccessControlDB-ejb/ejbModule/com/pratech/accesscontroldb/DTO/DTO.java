package com.pratech.accesscontroldb.DTO;

import java.io.Serializable;


/**
 * Clase con los atributos generales de los DTO
 * 
 * @since 2011-07-01
 * @author Diego Alberto Piedrahita
 * @email dpiedrahita@pratechgroup.com
 * @Company PRATECH S.A.S.
 */

@SuppressWarnings({ "serial" })
public abstract class DTO implements Serializable {

	private Long secuencial;

	/**
	 * @return the secuencial
	 */
	public Long getSecuencial() {
		return secuencial;
	}

	/**
	 * @param secuencial the secuencial to set
	 */
	public void setSecuencial(Long secuencial) {
		this.secuencial = secuencial;
	}

}
