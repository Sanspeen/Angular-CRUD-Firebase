package com.pratech.accesscontroldb.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Utilidades para tratar con excepciones.
 * 
 * @since 2012-05-08
 * @author Juan Esteban Jaramillo
 * @email jjaramillo@pratechgroup.com
 * @Company PRATECH S.A.S.
 * 
 */
public class ExceptionUtil {

	/**
	 * Retorna un string con el stacktrace de un throwable
	 * (excepciones o errores).
	 * 
	 * @param aThrowable
	 * @return
	 */
	public static String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}

}
