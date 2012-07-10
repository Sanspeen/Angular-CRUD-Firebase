package com.pratech.accesscontroldb.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.pratech.accesscontroldb.common.ACConfig;
import com.pratech.accesscontroldb.common.ACConstant;
import com.pratech.accesscontroldb.core.connection.ConnectionDB;

public class XMLData {

	/**
	 * Lee archivos XML referentes a fuente y número de fuente.
	 * 
	 * @return
	 */
	public List<String[]> readXML() {
		List<String[]> lisCombo = new ArrayList<String[]>(2);
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(ACConstant.PATH_FUENTE));
			
			NodeList nodeList = document.getElementsByTagName("data");
			String[] data = new String[nodeList.getLength()];
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element ele = (Element)nodeList.item(i);
				NodeList node = ele.getChildNodes();
				data[i] = ((Node) node.item(0)).getNodeValue().toString();
			}
			lisCombo.add(data);


			document = builder.parse(new File(ACConstant.PATH_NUFUENTE));
			nodeList = document.getElementsByTagName("data");
			data = new String[nodeList.getLength()];
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element ele = (Element)nodeList.item(i);
				NodeList node = ele.getChildNodes();
				data[i] = ((Node) node.item(0)).getNodeValue().toString();
			}			
			lisCombo.add(data);

			document = builder.parse(new File(ACConstant.PATH_EQUIPO));
			nodeList = document.getElementsByTagName("data");
			data = new String[nodeList.getLength()];
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element ele = (Element)nodeList.item(i);
				NodeList node = ele.getChildNodes();
				data[i] = ((Node) node.item(0)).getNodeValue().toString();
			}			
			lisCombo.add(data);

		} catch (SAXException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error XML al leer archivos XML (diferentes de XML de instancias)", e);
		} catch (IOException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error I/O al leer archivos XML (diferentes de XML de instancias)", e);
		} catch (ParserConfigurationException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error configuración de parser al leer archivos XML (diferentes de XML de instancias)", e);
		}
		return lisCombo;
	}

	/**
	 * Lee el archivo XML de instancias.
	 * 
	 * @param environmentName El ambiente para el cual se solicitan las instancias.
	 * @return
	 */
	public List<String> readXMLInstances(String environmentName) {
		List<String> instanceNames = new ArrayList<String>();
		String environment = ACConfig.getValue(environmentName);
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(ACConstant.PATH_INSTANCES));
			
			NodeList nodeList = document.getElementsByTagName("environment");
			for (int i = 0; i < nodeList.getLength(); i++) {				
				Element ele = (Element)nodeList.item(i);
				if (ele.getAttribute("id") != null && ele.getAttribute("id").equals(environment)) {
					NodeList list = ele.getChildNodes(); //Los hijos de "environment" son "instance"
					for (int j = 0; j < list.getLength(); j++) {
						boolean isElement = list.item(j) instanceof Element;
						if (!isElement)
							continue;
						Element elem = (Element)list.item(j);
						Element nameNode = (Element) elem.getElementsByTagName("name").item(0);
						NodeList name = nameNode.getChildNodes();
						instanceNames.add((name.item(0)).getNodeValue());
					}
				}
			}
		} catch (SAXException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error XML al leer archivo XML de instancias", e);
		} catch (IOException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error I/O al leer archivo XML de instancias", e);
		} catch (ParserConfigurationException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error configuración de parser al leer archivo XML de instancias", e);
		}
		return instanceNames;
	}
	
	/**
	 * Lee la lista de ambientes del archivo XML de instancias.
	 *
	 * @return La URL de la instancia indicada o null si no se encontró el dato.
	 */
	public List<String> getXMLAmbientes()
	{
		List<String> ambList = new ArrayList<String>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(ACConstant.PATH_INSTANCES));
			
			NodeList nodeList = document.getElementsByTagName("environment");
			for (int i = 0; i < nodeList.getLength(); i++) {				
				Element ele = (Element)nodeList.item(i);
				if (ele.getAttribute("id") != null) {
					String ambName = ACConfig.getValue(ele.getAttribute("id"));
					ambList.add(ambName);
				}
			}
		} catch (SAXException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error XML al leer archivo XML de instancias para ambientes", e);
		} catch (IOException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error I/O al leer archivo XML de instancias para ambientes", e);
		} catch (ParserConfigurationException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error configuración de parser al leer archivo XML de instancias para ambientes", e);
		}
		return ambList;
	}
	
	/**
	 * Lee la URL para un ambiente e instancia.
	 * 
	 * @param environment El ambiente para el cual se solicitan las instancias.
	 * @return La URL de la instancia indicada o null si no se encontró el dato.
	 */
	public String readURLXML(String environmentName, String instanceName) {

		String environment = ACConfig.getValue(environmentName);
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(ACConstant.PATH_INSTANCES));
			
			NodeList nodeList = document.getElementsByTagName("environment");
			for (int i = 0; i < nodeList.getLength(); i++) {				
				Element ele = (Element)nodeList.item(i);
				if (ele.getAttribute("id") != null && ele.getAttribute("id").equals(environment)) {
					NodeList list = ele.getChildNodes(); //Los hijos de "environment" son "instance"
					for (int j = 0; j < list.getLength(); j++) {
						boolean isElement = list.item(j) instanceof Element;
						if (!isElement)
							continue;
						Element elem = (Element)list.item(j);
						
						Element nameNode = (Element) elem.getElementsByTagName("name").item(0);
						NodeList name = nameNode.getChildNodes();
						if ((name.item(0)).getNodeValue().equals(instanceName)) {
							Element urlNode = (Element) elem.getElementsByTagName("url").item(0);
							NodeList url = urlNode.getChildNodes();
							return (url.item(0)).getNodeValue();
						}
					}
				}
			}
		} catch (SAXException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error XML al leer archivo XML de instancias para URL", e);
		} catch (IOException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error I/O al leer archivo XML de instancias para URL", e);
		} catch (ParserConfigurationException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error configuración de parser al leer archivo XML de instancias para URL", e);
		}
		return null;
	}
	
	/**
	 * Escribe el archivo XML con las instancias.
	 * 
	 * @param xmlInstances String con contenido archivo XML.
	 * @return ruta de archivo o null si el archivo no fue escrito.
	 */
	public String writeXMLInstances(String xmlInstances) {
		try {
			FileWriter fileWriter = new FileWriter(ACConstant.PATH_INSTANCES);
			fileWriter.write(xmlInstances);
			fileWriter.flush();
			fileWriter.close();
			return ACConstant.PATH_INSTANCES;
		} catch (IOException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error I/O al escribir archivo XML de instancias", e);
		}
		return null;
	}
	
	/**
	 * Valida el archivo XML con las instancias.
	 * 
	 * @param environment El ambiente para el cual se solicitan las instancias.
	 * @return La URL de la instancia indicada o null si no se encontró el dato.
	 */
	public String validateXMLInstances(String path) {
		
		List<String> idEnv = new ArrayList<String>();
		String validationMessage = "";
		if (path == null)
			path = ACConstant.PATH_INSTANCES;
		validationMessage = "Verifique el archivo " + path + ": ";
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(path));
			
			NodeList nodeList = document.getElementsByTagName("environment");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element ele = (Element)nodeList.item(i);
				String attId = ele.getAttribute("id");
				if (attId != null && attId.trim().length() > 0) {
					if (idEnv.contains(attId)) {
						validationMessage += "dos elementos 'environment' no deben tener el mismo valor para atributo 'id'.";
						return validationMessage;
					} else {
						idEnv.add(attId);
					}
				} else {
					validationMessage += "el atributo 'id' es obligatorio para todos los elementos 'environment'.";
					return validationMessage;
				}
			}
		} catch (SAXException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Se encontró error en formato de archivo XML de instancias", e);
			
			validationMessage += "por favor verifique que el formato del archivo XML este bien formado.";
			return validationMessage;
		} catch (FileNotFoundException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "No se encontró archivo de instancias para validarlo", e);

			validationMessage += "no se encuentra el archivo.";
			return validationMessage;
		} catch (IOException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error de I/O al leer archivo de instancias", e);
		} catch (ParserConfigurationException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error de configuración de parser al leer archivo de instancias", e);
		}
		return null;
	}
	
	/**
	 * Exporta las instancias como XML de la tabla que las contiene.
	 * 
	 * @param ambiente
	 * @return List
	 */
	public String updateInstancesXML() {
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String[] ambientes;
		int cdAmbiente;
		StringBuffer xmlInstancias = new StringBuffer();
		String xmlAmbientes;
		String message;
		String fileValidationMessage;
		FileWriter writer;
		File originalFile;
		File backupOriginalFile;
		File newFile;
		XMLData xmlData;

		try {
			ambientes = ACConfig.getValue("SincAmbientes").split(",");
			con = ConnectionDB.createConnection(new HashMap<String, String>());
			String StrSQLXML = ACConfig.getValue("SQLXMLIntanciasPorAmbiente");
			statement = con.prepareStatement(StrSQLXML);
			
			for (String ambiente : ambientes)
			{
				cdAmbiente = Integer.parseInt(ambiente.trim());
				statement.setInt(1, cdAmbiente);
				statement.setInt(2, cdAmbiente);
				boolean moreResults = statement.execute();
				if (moreResults)
				{
					rs = statement.getResultSet();
					rs.next();
					xmlInstancias.append(rs.getString(1));
					try {
						rs.close();
					} catch (Exception e) {
						//Registrar excepción
						e.printStackTrace();
						Store.getInstance().error("NA", "Error al cerrar conjunto de resultados", e);
					}
					rs = null;
				}
				statement.clearParameters();
			}
			xmlAmbientes = "<environments>" + xmlInstancias.toString() + "</environments>";
			
			newFile = new File(ACConstant.PATH_INSTANCES + ".new");
			backupOriginalFile = new File(ACConstant.PATH_INSTANCES + ".backup");
			writer = new FileWriter(newFile);
			writer.write(xmlAmbientes);
			writer.flush();
			writer.close();
			
			xmlData = new XMLData();
			fileValidationMessage = xmlData.validateXMLInstances(ACConstant.PATH_INSTANCES + ".new");
			if (fileValidationMessage != null) {
				message = "Fallo actualizando archivo de instancias: se encontraron errores en la validación del nuevo archivo generado. Mensaje de validación: '" + fileValidationMessage
				 + "'. Por ello dicho archivo no reemplazará el archivo actual de instancias, que se conservará.";
			} else {
				originalFile = new File(ACConstant.PATH_INSTANCES);
				backupOriginalFile.delete();
				if (originalFile.renameTo(backupOriginalFile)) {
					if (newFile.renameTo(originalFile)) {
						message = "Éxito actualizando archivo de instancias.";
					} else {
						message = "Fallo actualizando archivo de instancias: " + "no se pudo reemplazar el archivo de instancias original.";
					}
				} else {
					message = "Fallo actualizando archivo de instancias: " + "no se pudo respaldar el archivo de instancias original.";
				}
			}
		} catch (SQLException e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error SQL al actualizar archivo de instancias XML", e);

			message = "Fallo actualizando archivo de instancias: " + e.getMessage();
		} catch (Exception e) {
			//Registrar excepción
			e.printStackTrace();
			Store.getInstance().error("NA", "Error general al actualizar archivo de instancias XML", e);
			
			message = "Fallo actualizando archivo de instancias: " + e.getMessage();
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				//Registrar excepción
				e.printStackTrace();
				Store.getInstance().error("NA", "Error al cerrar conjunto de resultados", e);
			}
			rs = null;
			try {
				statement.close();
			} catch (Exception e) {
				//Registrar excepción
				e.printStackTrace();
				Store.getInstance().error("NA", "Error al cerrar sentencia", e);
			}
			statement = null;
			try {
				con.close();
			} catch (Exception e) {
				//Registrar excepción
				e.printStackTrace();
				Store.getInstance().error("NA", "Error al cerrar conexión", e);
			}
			con = null;
		}
		
		return message;
	}
	
}
