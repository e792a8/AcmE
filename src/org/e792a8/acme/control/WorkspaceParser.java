package org.e792a8.acme.control;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

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

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WorkspaceParser {

	private static DocumentBuilder docBuilder;
	private static Transformer transformer;
	private static IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();

	public static DocumentBuilder getDocumentBuilder() {
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

	public static Transformer getTransformer() {
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

	public static IPath getRoot() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocation();
	}

	private static Document readDoc(IPath path) {
		File file = path.append("acme.xml").toFile();
		if (!file.exists()) {
			initPath(path);
		}
		try {
			return getDocumentBuilder().parse(path.append("acme.xml").toFile());
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static void writeDoc(Document doc, IPath path) {
		File file = path.append("acme.xml").toFile();
		if (!file.exists()) {
			path.toFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			getTransformer().transform(new DOMSource(doc), new StreamResult(file));
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void initPath(IPath path) {
		Document doc = getDocumentBuilder().newDocument();
		Element directory = doc.createElement("directory");
		directory.setAttribute("type", "group");
		doc.appendChild(directory);
		writeDoc(doc, path);
	}

	public static IPath getParent(IPath path) {
		IPath parent = path.removeLastSegments(1);
		if (!getRoot().isPrefixOf(parent)) {
			return null;
		}
		return parent;
	}

	public static IPath[] getGroupChildren(IPath path) {
		Document doc = readDoc(path);
		Element directory = doc.getDocumentElement();
		if (!"group".equals(directory.getAttribute("type"))) {
			return null;
		}
		NodeList list = directory.getElementsByTagName("child");
		IPath[] children = new IPath[list.getLength()];
		for (int i = 0; i < list.getLength(); ++i) {
			children[i] = path.append(list.item(i).getAttributes().getNamedItem("path").getTextContent());
		}
		return children;
	}

	public static void addGroupChild(IPath path, String subpath) {
		Document doc = readDoc(path);
		Element directory = doc.getDocumentElement();
		if (!"group".equals(directory.getAttribute("type"))) {
			return;
		}
		Element child = doc.createElement("child");
		child.setAttribute("path", subpath);
		directory.appendChild(child);
		writeDoc(doc, path);
	}

	public static void removeGroupChild(IPath path, int index) {
		Document doc = readDoc(path);
		Element directory = doc.getDocumentElement();
		if (!"group".equals(directory.getAttribute("type"))) {
			return;
		}
		NodeList children = directory.getElementsByTagName("child");
		if (index > children.getLength()) {
			return;
		}
		directory.removeChild(children.item(index));
		writeDoc(doc, path);
	}

	public static String getAttribute(IPath path, String key) {
		Document doc = readDoc(path);
		Element directory = doc.getDocumentElement();
		return directory.getAttribute(key);

	}

	public static void setAttribute(IPath path, String key, String val) {
		Document doc = readDoc(path);
		Element directory = doc.getDocumentElement();
		directory.setAttribute(key, val);
		writeDoc(doc, path);
	}

	public static String[][] getProblemTestPoints(IPath path) {
		Document doc = readDoc(path);
		Element directory = doc.getDocumentElement();
		if (!"problem".equals(directory.getAttribute("type"))) {
			return null;
		}
		NodeList list = directory.getElementsByTagName("test");
		String[][] tests = new String[list.getLength()][2];
		for (int i = 0; i < list.getLength(); ++i) {
			tests[i][0] = ((Element) list.item(i)).getAttribute("in");
			tests[i][1] = ((Element) list.item(i)).getAttribute("ans");
		}
		return tests;
	}

	public static void addProblemTestPoint(IPath path, String in, String ans) {
		Document doc = readDoc(path);
		Element directory = doc.getDocumentElement();
		if (!"problem".equals(directory.getAttribute("type"))) {
			return;
		}
		Element test = doc.createElement("test");
		test.setAttribute("in", in);
		test.setAttribute("ans", ans);
		directory.appendChild(test);
		writeDoc(doc, path);
	}

	public static void removeProblemTestPoint(IPath path, int index) {
		Document doc = readDoc(path);
		Element directory = doc.getDocumentElement();
		if (!"problem".equals(directory.getAttribute("type"))) {
			return;
		}
		NodeList children = directory.getElementsByTagName("test");
		if (index > children.getLength()) {
			return;
		}
		directory.removeChild(children.item(index));
		writeDoc(doc, path);
	}

	public static Map<String, String> getProblemSolution(IPath path) {
		Document doc = readDoc(path);
		Element directory = doc.getDocumentElement();
		if (!"problem".equals(directory.getAttribute("type"))) {
			return null;
		}
		NodeList sols = directory.getElementsByTagName("solution");
		Element sol;
		Map<String, String> map = new TreeMap<>();
		if (sols.getLength() > 0) {
			sol = (Element) sols.item(0);
			NamedNodeMap list = sol.getAttributes();
			for (int i = 0; i < list.getLength(); ++i) {
				map.put(list.item(i).getNodeName(), list.item(i).getNodeValue());
			}
		}
		return map;
	}

	public static void setProblemSolution(IPath path, String key, String val) {
		Document doc = readDoc(path);
		Element directory = doc.getDocumentElement();
		if (!"problem".equals(directory.getAttribute("type"))) {
			return;
		}
		NodeList sols = directory.getElementsByTagName("solution");
		Element sol;
		if (sols.getLength() > 0) {
			sol = (Element) sols.item(0);
		} else {
			sol = doc.createElement("solution");
			directory.appendChild(sol);
		}
		sol.setAttribute(key, val);
		writeDoc(doc, path);
	}
}
