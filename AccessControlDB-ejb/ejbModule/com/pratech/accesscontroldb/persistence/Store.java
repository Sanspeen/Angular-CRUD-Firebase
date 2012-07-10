package com.pratech.accesscontroldb.persistence;

import com.pratech.accesscontroldb.DTO.LogSql;

import suramericana.cronos.cliente.loggerAuditoria.AuditoriaCronos;
import suramericana.cronos.cliente.logger.LoggerCronos;
import suramericana.cronos.excepciones.UndefinedAplicationException;

/**
 * Registro de auditoria de CRONOS
 * 
 * @since 2011-07-01
 * @author Diego Alberto Piedrahita
 * @email dpiedrahita@pratechgroup.com
 * @Company PRATECH S.A.S.
 * 
 */
public class Store {

	private String app_name = "ACCESSCONTROLDB";
	private AuditoriaCronos auditoriaCronos = new AuditoriaCronos(null, app_name);
	private LoggerCronos logger = new LoggerCronos(null, app_name);
	
	/**
	 * Constructor privado.
	 */
	private Store(){
		
	}
	
    /**
     * Obtiene la instancia del objeto
     *
     * @return Configuration
     */
    public static Store getInstance() {
        return StoreHolder.INSTANCE;
    }
 
    /**
     * Clase para gestión de la instancia única del objeto
     * como establece el patrón Singleton.
     *
     */
    private static class StoreHolder {
    	/**
    	 * Crea la instancia del objeto
    	 */
        private static final Store INSTANCE = new Store();
    }
	
	/**
	 * Guarda la auditoria
	 * 
	 * @param typeError
	 *            Tipo de error 1.Auditoría 2.Error 3.info
	 * @param logSql
	 */
	public void save(String typeError, LogSql logSql) {

		// Messages audit
		
		if (typeError.equals("1")) {

			if (logSql.getCod().length() < 1) {
				logSql.setCod("AC1");
			}

			if (logSql.getCamposTexto()[0].length() > 0) {
				auditoriaCronos.info(logSql.getUsuario(), logSql.getCod(),
						logSql.getTransaccion(),
						logSql.getDescripcionAudit(), logSql.getProceso(),
						logSql.getCamposTexto());

			} else {
				auditoriaCronos.info(logSql.getUsuario(), logSql.getCod(),
						Long.parseLong("1"),
						logSql.getDescripcionAudit(), logSql.getProceso());
			}
		}

		if (typeError.equals("2")) {
			if (logSql.getCod().length() < 1) {
				logSql.setCod("AC2");
			}
			try {

				if (logSql.getCamposTexto()[0].length() > 0) {
					auditoriaCronos.info(logSql.getUsuario(), logSql.getCod(),
							logSql.getTransaccion(),
							logSql.getDescripcionAudit(), logSql.getProceso(),
							logSql.getCamposTexto());
				} else {

					auditoriaCronos.info(logSql.getUsuario(), logSql.getCod(),
							Long.parseLong("1"),
							logSql.getDescripcionAudit(), logSql.getProceso());
				}

			} catch (UndefinedAplicationException e) {
				//Registrar excepción
				e.printStackTrace();
				error("NA", "Error al escribir registro AC2 en auditoria", e);
			}
		}

		if (typeError.equals("3")) {
			if (logSql.getCod().length() < 1) {
				logSql.setCod("AC3");
			}
			try {
				auditoriaCronos.info(logSql.getUsuario(), logSql.getCod(),
						logSql.getTransaccion(),
						logSql.getDescripcionAudit(), logSql.getProceso(),
						logSql.getCamposTexto());

			} catch (UndefinedAplicationException e) {
				//Registrar excepción
				e.printStackTrace();
				error("NA", "Error al escribir registro AC3 en auditoria", e);
			}
		}

		if (typeError.equals("4")) {
			if (logSql.getCod().length() < 1) {
				logSql.setCod("AC4");
			}
			try {
				logger.error(logSql.getCod(), logSql.getUsuario(),
						logSql.getDescripcionAudit(),
						logSql.getDescripcionAudit(), logSql.getThrowable());

			} catch (UndefinedAplicationException e) {
				//Registrar excepción
				e.printStackTrace();
				error("NA", "Error al escribir registro AC4 en auditoria", e);
			}
		}
		
		//Modificado el 2012-05-24 por Juan
		if (typeError.equals("5")) {
			if (logSql.getCod().length() < 1) {
				logSql.setCod("AC5");
	}
			try {
				auditoriaCronos.info(logSql.getUsuario(), logSql.getCod(),
						logSql.getTransaccion(),
						logSql.getDescripcionAudit(), logSql.getProceso(),
						logSql.getCamposTexto());

			} catch (UndefinedAplicationException e) {
				//Registrar excepción
				e.printStackTrace();
				error("NA", "Error al escribir registro AC5 en auditoria", e);
			}
		}
	}
	
	public void error(String usuario, String mensajeUsuario,
			Throwable excepcion) {
		logger.error("NA", usuario, mensajeUsuario, excepcion != null? excepcion.getMessage() : "", excepcion);
	}
	
}
