package org.e792a8.acme.core.workspace.internal;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

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

import org.e792a8.acme.core.workspace.DirectoryConfig;
import org.e792a8.acme.core.workspace.JudgeConfig;
import org.e792a8.acme.core.workspace.SolutionConfig;
import org.e792a8.acme.core.workspace.TestPointConfig;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

	static boolean writeDoc(Document doc, IPath path) {
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

	/**
	 * Reads DirectoryConfig from XML config file
	 * 
	 * @return null if fails to read XML file
	 */
	@Deprecated
	public static DirectoryConfig readDirConfig(IPath absPath) {
		DirectoryConfig handle = new DirectoryConfig();
		handle.absPath = absPath;
		Document doc = readDoc(absPath);
		if (doc == null) {
			return null;
		}
		Element elem = doc.getDocumentElement();
		if (!"directory".equals(elem.getTagName())) {
			return null;
		}
		handle.type = elem.getAttribute("type");
		handle.name = elem.getAttribute("name");
		handle.url = elem.getAttribute("url");
		if ("group".equals(handle.type)) {
			NodeList children = elem.getElementsByTagName("child");
			handle.children = new LinkedList<>();
			for (int i = 0; i < children.getLength(); ++i) {
				handle.children.add(((Element) children.item(i)).getAttribute("path"));
			}
		} else if ("problem".equals(handle.type)) {
			Element judgeElem = (Element) elem.getElementsByTagName("judge").item(0);
			handle.judge = new JudgeConfig();
			handle.judge.type = judgeElem.getAttribute("type");
			// TODO JudgeConfig attributes for other types
			NodeList solutions = elem.getElementsByTagName("solution");
			SolutionConfig sol = new SolutionConfig();
			sol.directory = handle;
			sol.lang = ((Element) solutions.item(0)).getAttribute("lang");
			sol.path = ((Element) solutions.item(0)).getAttribute("path");
			handle.solution = sol;
			NodeList tests = elem.getElementsByTagName("test");
			handle.testPoints = new LinkedList<>();
			for (int i = 0; i < tests.getLength(); ++i) {
				TestPointConfig test = new TestPointConfig();
				test.directory = handle;
				test.in = ((Element) tests.item(i)).getAttribute("in");
				test.ans = ((Element) tests.item(i)).getAttribute("ans");
				handle.testPoints.add(test);
			}
		} else {
			return null;
		}
		return handle;
	}

	/**
	 * Writes DirectoryConfig to XML config file
	 * 
	 * @return false if fails to write XML file
	 */
	@Deprecated
	public static boolean writeDirConfig(DirectoryConfig handle) {
		Document doc = getDocumentBuilder().newDocument();
		Element elem = doc.createElement("directory");
		doc.appendChild(elem);
		elem.setAttribute("type", handle.type);
		elem.setAttribute("name", handle.name);
		elem.setAttribute("url", handle.url);
		if ("group".equals(handle.type)) {
			Iterator<String> itr = handle.children.iterator();
			while (itr.hasNext()) {
				Element child = doc.createElement("child");
				child.setAttribute("path", itr.next());
				elem.appendChild(child);
			}
		} else if ("problem".equals(handle.type)) {
			Element j = doc.createElement("judge");
			j.setAttribute("type", handle.judge.type);
			// TODO handle attributes for other types of judge
			elem.appendChild(j);
			SolutionConfig sol = handle.solution;
			Element e = doc.createElement("solution");
			e.setAttribute("lang", sol.lang);
			e.setAttribute("path", sol.path);
			elem.appendChild(e);
			Iterator<TestPointConfig> itrT = handle.testPoints.iterator();
			while (itrT.hasNext()) {
				TestPointConfig test = itrT.next();
				e = doc.createElement("test");
				e.setAttribute("in", test.in);
				e.setAttribute("ans", test.ans);
				elem.appendChild(e);
			}
		} else {
			return false;
		}
		return writeDoc(doc, handle.absPath);
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
