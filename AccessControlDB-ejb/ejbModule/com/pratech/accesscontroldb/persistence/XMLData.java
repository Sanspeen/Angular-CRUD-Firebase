package com.pratech.accesscontroldb.persistence;

import java.io.File;
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

import com.pratech.accesscontroldb.common.ACConstant;

public class XMLData {

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

}
