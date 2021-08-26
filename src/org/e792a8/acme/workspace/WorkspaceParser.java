package org.e792a8.acme.workspace;

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

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

	public static boolean writeDir(DirectoryHandle handle) {
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
			Iterator<SolutionHandle> itrS = handle.solutions.iterator();
			while (itrS.hasNext()) {
				SolutionHandle sol = itrS.next();
				Element e = doc.createElement("solution");
				e.setAttribute("lang", sol.lang);
				e.setAttribute("path", sol.path);
				elem.appendChild(e);
			}
			Iterator<TestPointHandle> itrT = handle.testPoints.iterator();
			while (itrT.hasNext()) {
				TestPointHandle test = itrT.next();
				Element e = doc.createElement("test");
				e.setAttribute("in", test.in);
				e.setAttribute("ans", test.ans);
				elem.appendChild(e);
			}
		} else {
			return false;
		}
		return writeDoc(doc, handle.absPath);
	}

	public static DirectoryHandle readDir(IPath absPath) {
		DirectoryHandle handle = new DirectoryHandle();
		handle.absPath = absPath;
		Document doc = readDoc(absPath);
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
			NodeList solutions = elem.getElementsByTagName("solution");
			handle.solutions = new LinkedList<>();
			for (int i = 0; i < solutions.getLength(); ++i) {
				SolutionHandle sol = new SolutionHandle();
				sol.lang = ((Element) solutions.item(i)).getAttribute("lang");
				sol.path = ((Element) solutions.item(i)).getAttribute("path");
				handle.solutions.add(sol);
			}
			NodeList tests = elem.getElementsByTagName("test");
			handle.testPoints = new LinkedList<>();
			for (int i = 0; i < tests.getLength(); ++i) {
				TestPointHandle test = new TestPointHandle();
				test.in = ((Element) tests.item(i)).getAttribute("in");
				test.ans = ((Element) tests.item(i)).getAttribute("ans");
				handle.testPoints.add(test);
			}
		} else {
			return null;
		}
		return handle;
	}

	private static Document readDoc(IPath path) {
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

	private static boolean writeDoc(Document doc, IPath path) {
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

	public static IPath getParent(IPath path) {
		IPath parent = path.removeLastSegments(1);
		if (!getRoot().isPrefixOf(parent)) {
			return null;
		}
		return parent;
	}

}
