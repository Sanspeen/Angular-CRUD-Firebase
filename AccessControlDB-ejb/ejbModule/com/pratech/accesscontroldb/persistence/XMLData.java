package com.pratech.accesscontroldb.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

		} catch (SAXException ex) {
			Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (IOException ex) {
			Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (ParserConfigurationException ex) {
			Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null,
					ex);
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
		} catch (SAXException ex) {
			Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (IOException ex) {
			Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (ParserConfigurationException ex) {
			Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null,
					ex);
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
		} catch (SAXException ex) {
			Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (IOException ex) {
			Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (ParserConfigurationException ex) {
			Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null,
					ex);
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
		} catch (SAXException ex) {
			Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (IOException ex) {
			Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (ParserConfigurationException ex) {
			Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null,
					ex);
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
		} catch (IOException ex) {
			Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null,
					ex);
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
		} catch (SAXException ex) {
			Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null,
					ex);
			validationMessage += "por favor verifique que el formato del archivo XML este bien formado.";
			return validationMessage;
		} catch (FileNotFoundException ex) {
			Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null,
					ex);
			validationMessage += "no se encuentra el archivo.";
			return validationMessage;
		} catch (IOException ex) {
			Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (ParserConfigurationException ex) {
			Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return null;
	}
	
}
