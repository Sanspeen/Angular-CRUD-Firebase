package com.pratech.accesscontroldb.persistence;

import com.pratech.accesscontroldb.DTO.LogSql;
import com.pratech.accesscontroldb.common.ACConfig;

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
			AuditoriaCronos logger = null;
			try {
				logger = new AuditoriaCronos(null, "ACCESSCONTROLDB");
			} catch (UndefinedAplicationException ex) {
				ex.getLocalizedMessage();
			}
			if (logSql.getCamposTexto()[0].length() > 0) {
				logger.info(logSql.getUsuario(), logSql.getCod(),
						Long.parseLong(ACConfig.getValue("transaccion")),
						logSql.getDescripcionAudit(), logSql.getProceso(),
						logSql.getCamposTexto());

			} else {
				logger.info(logSql.getUsuario(), logSql.getCod(),
						Long.parseLong(ACConfig.getValue("transaccion")),
						logSql.getDescripcionAudit(), logSql.getProceso());
			}
		}

		if (typeError.equals("2")) {
			if (logSql.getCod().length() < 1) {
				logSql.setCod("AC2");
			}
			AuditoriaCronos logger = new AuditoriaCronos(null,
					"ACCESSCONTROLDB");
			try {

				if (logSql.getCamposTexto()[0].length() > 0) {
					logger.info(logSql.getUsuario(), logSql.getCod(),
							Long.parseLong(ACConfig.getValue("transaccion")),
							logSql.getDescripcionAudit(), logSql.getProceso(),
							logSql.getCamposTexto());
				} else {

					logger.info(logSql.getUsuario(), logSql.getCod(),
							Long.parseLong(ACConfig.getValue("transaccion")),
							logSql.getDescripcionAudit(), logSql.getProceso());
				}

			} catch (UndefinedAplicationException e) {

				e.getLocalizedMessage();
			}
		}

		if (typeError.equals("3")) {
			if (logSql.getCod().length() < 1) {
				logSql.setCod("AC3");
			}

			AuditoriaCronos loggerCronos = new AuditoriaCronos(null,
					"ACCESSCONTROLDB");
			try {
				loggerCronos.info(logSql.getUsuario(), logSql.getCod(),
						Long.parseLong(ACConfig.getValue("transaccion")),
						logSql.getDescripcionAudit(), logSql.getProceso(),
						logSql.getCamposTexto());

			} catch (UndefinedAplicationException e) {

				e.getLocalizedMessage();
			}
		}

		if (typeError.equals("4")) {
			if (logSql.getCod().length() < 1) {
				logSql.setCod("AC4");
			}

			LoggerCronos logger = new LoggerCronos(null, "ACCESSCONTROLDB");
			try {
				logger.error(logSql.getCod(), logSql.getUsuario(),
						logSql.getDescripcionAudit(),
						logSql.getDescripcionAudit(), logSql.getThrowable());

			} catch (UndefinedAplicationException e) {
				e.getLocalizedMessage();
			}
		}
	}
}
