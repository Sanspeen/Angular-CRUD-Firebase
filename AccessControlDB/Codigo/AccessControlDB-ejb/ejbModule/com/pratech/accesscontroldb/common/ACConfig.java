
package com.pratech.accesscontroldb.common;

/**
 * Clase con utilidades para la aplicaci�n
 * 
 * @author Diego Piedrahita - Pratech S.A.S
 * @since 2011-07-01
 * 
 */
public class ACConfig {
	
	/**
     * Obtiene el valor del parametro en el archivo de configuraci�n.
     * 
     * @param parameter
     * @return String 
     */
    public static String getValue(String parameter) {
        
    	return Configuration.getInstance().getProperty(parameter);
    }
}
