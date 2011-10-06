package com.pratech.accesscontroldb.common;

import java.sql.Types;
import java.util.ResourceBundle;

/**
 * Clase con las utilidades de la aplicacion
 * 
 * @author dpiedrahita
 * @email dpiedrahita@pratechgroup.com
 * @since 20110701
 * @Company PRATECH S.A.S.
 * 
 */
public class ACUtil {

	/**
	 * Obtiene las propiedades a leer a partir de archivo properties
	 * 
	 * @param parametro
	 *            a buscar en archivo de propieades
	 * @return
	 */
	public static String getProperties(String parameter) {
		String resu = ResourceBundle.getBundle(
				"com/pratech/accesscontroldb/commons/confi").getString(
				parameter);
		return resu;
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public static int getType(String type) {

		if (type.toUpperCase().equals("VARCHAR2")) {
			return Types.VARCHAR;
		}
		if (type.toUpperCase().equals("NUMBER")) {
			return Types.NUMERIC;
		}
		if (type.toUpperCase().equals("DATE")) {
			return Types.DATE;
		}
		if (type.toUpperCase().equals("CHAR")) {
			return Types.CHAR;
		}
		if (type.toUpperCase().equals("LONG")) {
			return Types.FLOAT;
		}
		if (type.toUpperCase().equals("BOOLEAN")) {
			return Types.BOOLEAN;
		}
		if (type.toUpperCase().equals("BINARY_INTEGER")) {
			return Types.BINARY;
		}
		if (type.toUpperCase().equals("CLOB")) {
			return Types.CLOB;
		}

		return -1;
	}
}
