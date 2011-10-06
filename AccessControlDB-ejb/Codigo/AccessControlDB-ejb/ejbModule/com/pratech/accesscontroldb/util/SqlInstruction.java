package com.pratech.accesscontroldb.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pratech.accesscontroldb.DTO.BlocksVariable;
import com.pratech.accesscontroldb.DTO.RequestDTO;
import com.pratech.accesscontroldb.common.ACConfig;

/**
 * Evalua si se debe quitar el terminador del comando, esto dependiendo del tipo
 * de SQL
 * 
 * @since 2011-07-01
 * @author Diego Alberto Piedrahita
 * @email dpiedrahita@pratechgroup.com
 * @Company PRATECH S.A.S.
 * 
 */
public class SqlInstruction {

	/** Identifica el comando delimitador de sentencias SQL */
	private String commandTerminator = ACConfig.getValue("commandTerminator");

	/**
	 * Convierte la consulta SQL
	 * 
	 * @param requestDTO
	 *            = DTO with data from the view
	 * @return DTO with the response data for the view
	 */
	public RequestDTO editSql(RequestDTO requestDTO) {
		requestDTO.setStringSQL(requestDTO.getStringSQL().replaceAll(
				"[\n\r\t]", " "));

		int jj = requestDTO.getStringSQL().toUpperCase().indexOf("BEGIN");
		if (jj < 0) {
			if (requestDTO.getStringSQL().trim()
					.equalsIgnoreCase(commandTerminator)
					|| requestDTO.getStringSQL().trim()
							.endsWith(commandTerminator)) {
				requestDTO.setStringSQL(requestDTO.getStringSQL().substring(
						0,
						requestDTO.getStringSQL().length()
								- commandTerminator.length()));
			}
			requestDTO.setTypeSql(0);
		} else {
			requestDTO.setTypeSql(1);
		}

		return requestDTO;
	}

	/**
	 * Devuelve el nombre de la tabla en una consulta SQL
	 * 
	 * @param sql
	 *            = sqlstatement
	 * @return
	 */
	public static String getTableFromSql(String sql) {
		// Expresion para obtener el texto que precede al texto de la tabla
		String rexp = "^(select)\\s+.*\\s+(from)\\s+";
		// Compilación de expresion con CASE_INSENSITIVE
		Pattern p = Pattern.compile(rexp, Pattern.CASE_INSENSITIVE);

		// Objeto de coincidencias del patron en la cadena
		// nota. Se limpia previamente la cadena de caracteres
		// espacio, fin de linea, retorno de carro, tabuladores
		// para optimizar el proceso
		Matcher sqlComp = p.matcher(sql.replaceAll("\\s", " "));

		// Se reemplaza por vacio el texto que antecede al nombre de la tabla
		String resultado = sqlComp.replaceAll("");

		// Expresión para limpiar el texto que precede al nombre de la
		// tabla.
		rexp = "(,)?\\s.*";
		p = Pattern.compile(rexp);

		sqlComp = p.matcher(resultado);
		resultado = sqlComp.replaceAll("");
		return resultado;
	}

	/**
	 * Asigna los valores digitados en la tabla de variables a su referente en
	 * el bloque anonimo digitado
	 * 
	 * @param str
	 *            SQL Statement
	 * @param lisBlock
	 *            List of varibles with its value
	 * @return
	 */
	public static String replaceTextBlock(String str,
			List<BlocksVariable> lisBlock) {
		for (BlocksVariable block : lisBlock) {
			if (block.getAddress() != null && block.isUse()) {
				if (block.getAddress().equals("IN")) {
					Pattern patron = Pattern.compile(
							"(:" + block.getVariable().toUpperCase().trim()
									+ "\\w)", Pattern.CASE_INSENSITIVE);
					Matcher encaja = patron.matcher(str);
					if (block.getTypeData().equals("VARCHAR2")
							|| block.getTypeData().equals("CHAR")) {
						str = encaja.replaceAll("'" + block.getValue().trim()
								+ "'");
					} else {
						str = encaja.replaceAll(block.getValue().trim());
					}
				} else if (block.getAddress().equals("OUT")) {
					Pattern patron = Pattern.compile("(:"
							+ block.getVariable().trim() + "\\w)",
							Pattern.CASE_INSENSITIVE);
					Matcher encaja = patron.matcher(str);
					str = encaja.replaceAll("?");

				}
			}
		}
		return str;
	}
}
