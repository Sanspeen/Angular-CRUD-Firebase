package com.pratech.accesscontroldb.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.google.gwt.user.client.Window;
import com.pratech.accesscontroldb.DTO.BlocksVariable;
import com.pratech.accesscontroldb.DTO.RequestDTO;
import com.pratech.accesscontroldb.client.ACDBException;
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
	private Map<String, String> mapString = new HashMap<String, String>();

	/**
	 * Determina el tipo de sentencia SQL, si es:
	 * - sqlType 0: Sentencia sql de una sola instrucción
	 * - sqlType 1: Bloque anónimo
	 * - sqlType 2: Bloque de consultas
	 * Conserva lo que está dentro de comillas simples por medio de un
	 * reemplazo de dichos valores con un placeholder PR@XI@S + consecutivo
	 * y luego deshace éste reemplazo para que quede en su estado original.
	 * 
	 * @param requestDTO
	 *            = DTO with data from the view
	 * @return DTO with the response data for the view
	 * @throws ACDBException 
	 */
	public RequestDTO editSql(RequestDTO requestDTO,
			Map<String, String> dataInstance) throws ACDBException {

		//Reemplaza valores entre comillas simples por PR@XI@S + consecutivo
		String stringSQL = getTextString(requestDTO.getStringSQL());

		//Si jj < 0, no es un bloque anonimo
		int jj = stringSQL.toUpperCase().indexOf("BEGIN");
		if (jj < 0) {
			//En caso que no sea un bloque anonimo, 
			//se determina si se trata de un bloque de consulas
			//o de una sentencia sql con una sola instruccion

			String[] vecBloc = null;
			// Identifica si modifico la cosulta para quitarle el ultimo ;
			boolean mod = false;

			if (stringSQL.trim().indexOf(";") >= 0) {
				//Si existe al menos un punto y coma, éste debe estar
				//seguido por un salto de línea o espacio en blanco
				//o tabulador para considerarse que después
				//de él inicia una nueva instrucción.
				String tem = stringSQL.trim();
				vecBloc = tem.split(";[\n\r\t\\s]");

				for (int i = 0; i < vecBloc.length; i++) {
					if (vecBloc[i].endsWith(commandTerminator)) {
						//Si termina con ";", se quita el ";"
						//pues oracle no lo recibe. Ésto se hace
						//en general para la última línea pues en ella
						//el punto y coma no va seguido de un espacio, tabulador
						//o salto de linea.
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
					//Marca el comando como un bloque de consultas
					requestDTO.setTypeSql(2);
					requestDTO.setBlockSQL(vecBloc);
				} else {
					//Marca el comando como una sola consulta
					if (mod) {
						//Reemplaza las variables en una sola consulta
						stringSQL = setTextString(vecBloc[0]);
					}
					requestDTO.setTypeSql(0);
				}
				//Reemplaza las variables en el bloque de consultas
				requestDTO.setBlockSQL(setTextStringBlock(vecBloc));
			} else {
				requestDTO.setTypeSql(0);
			}
		} else {
			//En caso que sea un bloque anónimo, lo marca como tal
			requestDTO.setTypeSql(1);
		}

		if (requestDTO.isSQLServer()
				&& stringSQL.toUpperCase().indexOf("ROWID") > 0) {
			//Agrega los campos de la llave primaria a la consulta las
			//consultas en SQL server, pues éste no maneja ROWID
			requestDTO = insertRowid(dataInstance, stringSQL, requestDTO);
		}
		if (requestDTO.getBlockSQL() == null
				|| requestDTO.getBlockSQL().length < 1) {
			//Reemplaza las variables en una sola consulta
			//en el caso que no lleva ;
			//Nota: la segunda condición puede ser innecesaria
			//requestDTO.getBlockSQL().length < 1
		requestDTO.setStringSQL(setTextString(stringSQL));
		} else {
			requestDTO.setStringSQL(stringSQL);
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
				System.out.println(matcher.end() + "-" + matcher.regionEnd()
						+ "-" + matcher.regionStart() + "-" + matcher.start());
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
				// vec[i] = vec[i].trim().replaceAll("[\n\r\t]", " ");
			}
		}
		temp = "";
		for (int i = 0; i < vec.length; i++) {
			temp += vec[i];
			temp += "\r\n";
		}
		return temp.trim();
	}

	private RequestDTO insertRowid(Map<String, String> dataInstance,
			String stringSQL, RequestDTO requestDTO) throws ACDBException {
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

	private String getTextString(String SQL) {
		// Pattern p1 = Pattern.compile("'+[\\w\\W]+;[^']*'");
		Pattern p1 = Pattern.compile("('')|('[\\w\\W][^']*')");
		Matcher m1 = p1.matcher(SQL);
		StringBuffer sb = new StringBuffer();
		int i = 0;
		
		while (m1.find()) {
			m1.appendReplacement(sb, "'PR@XI@S" + i + "'");
			mapString.put("'PR@XI@S" + i + "'", m1.group());
			i++;
		}
		m1.appendTail(sb);
		return sb.toString();
	}

	private String setTextString(String SQL) {
		// Quitar Saltos			
		SQL = deleteComments(SQL);
		SQL = SQL.replaceAll("[\n\r\t]", " ");
		Iterator it = mapString.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			String q = e.getKey().toString();
			String w = e.getValue().toString();
			
			if(w.indexOf("\\") >= 0){						
				String aux = "";
				String vep[] = w.split("\\\\");
				for (int i = 0; i < vep.length; i++) {
					aux += vep[i] + "\\\\";				
				}

				w = aux;
				w = w.substring(0, w.length() - 2);
			}		
			
			if (w.indexOf("$") >= 0) {			

				String tem = "";
				String vec[] = w.split("\\$");
				for (int i = 0; i < vec.length; i++) {
					tem += vec[i] + "\\$";
				}

				w = tem;
				w = w.substring(0, w.length() - 2);
				
			}
			
			SQL = SQL.replaceAll(q, w);
			
		}
		return SQL;
	}

	private String[] setTextStringBlock(String SQLBlock[]) {
		for (int i = 0; i < SQLBlock.length; i++) {
			// Quitar Saltos
			SQLBlock[i] = deleteComments(SQLBlock[i]);
			SQLBlock[i] = SQLBlock[i].replaceAll("[\n\r\t]", " ");
			Pattern p1 = Pattern.compile("'PR@XI@S\\d*'");
			Matcher m1 = p1.matcher(SQLBlock[i]);
			StringBuffer sb = new StringBuffer();
			while (m1.find()) {
				String key = m1.group();
				String value = mapString.get(key);
				
				if(value.indexOf("\\") >= 0){						
					String auxo = "";
					String vp[] = value.split("\\\\");
					for (int j = 0; j < vp.length; j++) {
						auxo += vp[j] + "\\\\";				
					}

					value = auxo;
					value = value.substring(0, value.length() - 2);
				}
				
				if (value.indexOf("$") >= 0) {
					String tem=""; 
					String vec[] = value.split("\\$");					 

					for (int j = 0; j < vec.length; j++) {
						tem += vec[j] + "\\$";
					}	
					
					value = tem;
					value = value.substring(0, value.length() - 2);
				}				
				
				m1.appendReplacement(sb, value);
			}
			m1.appendTail(sb);
			SQLBlock[i] = sb.toString();
		}
		return SQLBlock;
	}

}