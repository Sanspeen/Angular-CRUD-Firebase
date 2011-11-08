package com.pratech.accesscontroldb.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Clase para el manejo de configuraciones del programa
 * 
 * @since   2011-07-01
 * @author  Andr�s Felipe Tejada
 * @email   atejada@pratechgroup.com
 * @Company PRATECH S.A.S.
 * 
 */
public class Configuration {
	
	/** Objeto Properties con las configuraciones del programa */
    Properties properties = null;
 
    /**
     * Contructor privado, carga el archivo de configuraci�n en el objeto properties
     */
    private Configuration() {
        this.properties = new Properties();
        try {
        	InputStream is = null;
        	File fileConfig = new File(ACConstant.PATH_CONFIG);
    		is = new FileInputStream(fileConfig.getPath());
    		
    		properties.load(is);
    		
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Obtiene la instancia del objeto
     *
     * @return Configuration
     */
    public static Configuration getInstance() {
        return ConfigurationHolder.INSTANCE;
    }
 
    /**
     * Clase para gesti�n de la instancia �nica del objeto
     * como se establece el patr�n Singleton
     *
     */
    private static class ConfigurationHolder {
    	/**
    	 * Crea la instancia del objeto
    	 */
        private static final Configuration INSTANCE = new Configuration();
    }
 
    /**
     * Retorna la propiedad de configuraci�n solicitada
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }
}