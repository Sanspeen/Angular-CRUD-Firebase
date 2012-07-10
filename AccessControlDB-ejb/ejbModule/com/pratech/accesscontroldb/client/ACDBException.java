package com.pratech.accesscontroldb.client;

import java.io.Serializable;

/**
 * Esta es una excepcion que permite mostrar
 * el mensaje de la excepcion en la interfaz
 * de usuario.
 * 
 * @author Juan Esteban Jaramillo - Pratech S.A.S.
 * @since 2012-05-14
 * 
 */
public class ACDBException extends Exception implements Serializable {

	private static final long serialVersionUID = 5829346594750871548L;

	private String message;
	
	public ACDBException() {
	}
	
	public ACDBException(String s) {
		message = s;
	}
	
	public String getMessage() {
		return message;
	}
	
}
