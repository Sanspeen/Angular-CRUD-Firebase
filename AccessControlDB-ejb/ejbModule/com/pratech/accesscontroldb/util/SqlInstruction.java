package com.pratech.accesscontroldb.util;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pratech.accesscontroldb.DTO.BlocksVariable;
import com.pratech.accesscontroldb.DTO.RequestDTO;
import com.pratech.accesscontroldb.common.ACConfig;
import com.pratech.accesscontroldb.core.ad.JdbcUtil;

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
	public RequestDTO editSql(RequestDTO requestDTO,
			Map<String, String> dataInstance) {

		int jj = requestDTO.getStringSQL().toUpperCase().indexOf("BEGIN");
		if (jj < 0) {
			String[] vecBloc = null;
			// Identifica si modifico la cosulta para quitarle el ultimo ;
			boolean mod = false;
			if (requestDTO.getStringSQL().trim().indexOf(";") >= 0) {

				String tem = requestDTO.getStringSQL().trim();
				vecBloc = tem.split(";[\n\r\t\\s]");

				for (int i = 0; i < vecBloc.length; i++) {
					if (vecBloc[i].startsWith("\r")) {
						vecBloc[i] = vecBloc[i].substring(1,
								vecBloc[i].length());
						if (vecBloc[i].startsWith("\n")) {
							vecBloc[i] = vecBloc[i].substring(1,
									vecBloc[i].length());
						}
					} else if (vecBloc[i].startsWith("\n")) {
						vecBloc[i] = vecBloc[i].substring(1,
								vecBloc[i].length());
					}
					if (vecBloc[i].endsWith(commandTerminator)) {
						mod = true;
						vecBloc[i] = vecBloc[i].substring(
								0,
								vecBloc[i].length()
										- commandTerminator.length());
					}
				}
			}
			if (vecBloc != null) {
				if (vecBloc.length > 1) {
					requestDTO.setTypeSql(2);
					requestDTO.setBlockSQL(vecBloc);
				} else {
					if (mod) {
						requestDTO.setStringSQL(vecBloc[0]);
					}
					requestDTO.setTypeSql(0);
				}
			} else {
				requestDTO.setTypeSql(0);
			}
		} else {
			requestDTO.setTypeSql(1);
		}

		if (requestDTO.isSQLServer()
				&& requestDTO.getStringSQL().toUpperCase().indexOf("ROWID") > 0) {
			requestDTO = insertRowid(dataInstance, requestDTO.getStringSQL(),
					requestDTO);
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
					Pattern patron = Pattern.compile("(:"
							+ block.getVariable().toUpperCase().trim() + ")",
							Pattern.CASE_INSENSITIVE);
					Matcher encaja = patron.matcher(str);
					if (block.getTypeData().equals("VARCHAR2")
							|| block.getTypeData().equals("CHAR")
							|| block.getTypeData().equals("")) {
						str = encaja.replaceAll("'" + block.getValue().trim()
								+ "'");
					} else {
						str = encaja.replaceAll(block.getValue().trim());
					}
				} else if (block.getAddress().equals("OUT")) {
					Pattern patron = Pattern.compile("(:"
							+ block.getVariable().trim() + " )",
							Pattern.CASE_INSENSITIVE);
					Matcher encaja = patron.matcher(str);
					str = encaja.replaceAll("?");

				}
			}
		}
		return str;
	}

	/**
	 * Elimina los comentarios de la cadena SQL
	 * 
	 * @param SQL
	 *            Instruccion SQL
	 * @return Instruccion SQL sin comentarios
	 */
	public static String deleteComments(String SQL) {
		String temp = "";
		temp = SQL.replaceAll("(\\/\\*)+[a-z\\s\n(--)]*(\\*\\/)", "");
		String[] vec = temp.split("[\r\n]");

		for (int i = 0; i < vec.length; i++) {
			Pattern pattern = Pattern.compile("'+.*(--)+.*?'");
			Matcher matcher = pattern.matcher(vec[i]);

			if (matcher.find()) {
				Pattern pattern1 = Pattern.compile("--");
				Matcher matcher1 = pattern1.matcher(vec[i]);
				while (matcher1.find()) {
					if (matcher1.start() < matcher.start() + 1) {
						vec[i] = vec[i].replaceAll("(--)+.*+([\r\n]|$)", "");
					} else {
						if (matcher1.start() > matcher.start() + 1) {
							if (matcher1.start() < vec[i].length()) {
								vec[i] = vec[i].substring(0, matcher1.start());
							}
						}
					}
				}
			} else {
				vec[i] = vec[i].replaceAll("(--)+.*+([\r\n]|$)", "");
				vec[i] = vec[i].trim().replaceAll("[\n\r\t]", " ");
			}
		}
		temp = "";
		for (int i = 0; i < vec.length; i++) {
			temp += vec[i];
			temp += " ";
		}
		return temp.trim();
	}

	private RequestDTO insertRowid(Map<String, String> dataInstance,
			String stringSQL, RequestDTO requestDTO) {
		JdbcUtil jdbcUtil = new JdbcUtil();
		String[] keys = jdbcUtil.getPrimaryKeys(dataInstance, stringSQL);

		String columKey = "";
		for (int i = 0; i < keys.length; i++) {
			columKey += keys[i].trim() + ",";
		}
		columKey = columKey.substring(0, columKey.length() - 1);

		Pattern pattern = Pattern.compile("rowid", Pattern.CASE_INSENSITIVE);
		Matcher encaja = pattern.matcher(stringSQL);
		requestDTO.setStringSQL(encaja.replaceAll(columKey));
		requestDTO.setColumnRowid(keys);
		return requestDTO;
	}
}