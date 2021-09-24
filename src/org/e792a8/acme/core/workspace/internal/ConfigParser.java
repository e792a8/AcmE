package org.e792a8.acme.core.workspace.internal;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigParser {

	private static DocumentBuilder docBuilder;
	private static Transformer transformer;

	private static DocumentBuilder getDocumentBuilder() {
		if (docBuilder == null) {
			try {
				docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return docBuilder;
	}

	private static Transformer getTransformer() {
		if (transformer == null) {
			try {
				transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return transformer;
	}

	static Document readDoc(IPath path) {
		File file = path.append("acme.xml").toFile();
		if (!file.exists()) {
			return null;
		}
		try {
			return getDocumentBuilder().parse(path.append("acme.xml").toFile());
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static void cleanNodes(Node node) {
		NodeList nl = node.getChildNodes();
		for (int i = nl.getLength() - 1; i >= 0; --i) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.TEXT_NODE) {
				node.removeChild(n);
			} else {
				cleanNodes(n);
			}
		}
	}

	static boolean writeDoc(Document doc, IPath path) {
		cleanNodes(doc.getDocumentElement());
		File file = path.append("acme.xml").toFile();
		if (!file.exists()) {
			path.toFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		try {
			getTransformer().transform(new DOMSource(doc), new StreamResult(file));
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	static Document newDocument() {
		return getDocumentBuilder().newDocument();
	}

	static String getAttribute(IPath location, String key) {
		return ((Element) readDoc(location)
			.getElementsByTagName("directory").item(0)).getAttribute(key);
	}

	public static boolean testRoot() {
		IPath rootLoc = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		Document doc = readDoc(rootLoc);
		if (doc != null) {
			Element elem = doc.getDocumentElement();
			if ("directory".equals(elem.getTagName())
				&& "group".equals(elem.getAttribute("type"))) {
				return true;
			}
		}
		doc = newDocument();
		Element elem = doc.createElement("directory");
		elem.setAttribute("type", "group");
		elem.setAttribute("name", "ROOT");
		elem.setAttribute("url", "");
		doc.appendChild(elem);
		writeDoc(doc, rootLoc);
		return true;
	}

}
