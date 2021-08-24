package org.e792a8.acme.control;

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

import org.e792a8.acme.control.problems.ProblemGroup;
import org.e792a8.acme.control.problems.ProblemObject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WorkspaceParser {

	private static DocumentBuilder docBuilder;
	private static Transformer transformer;

	private static void initBuilder() {
		if (transformer == null) {
			try {
				transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (docBuilder == null) {
			try {
				docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static ProblemGroup readRoot() {
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IPath rootPath = workspaceRoot.getLocation();
		ProblemGroup rootGroup = (ProblemGroup) readDir(rootPath,
			workspaceRoot.getFullPath().toString());
		if (rootGroup == null) {
			rootGroup = new ProblemGroup("ROOT");
			rootGroup.setRelPath("./");
			rootGroup.setAbsPath(rootPath);
			writeDir(rootGroup, rootPath);
		}
		return rootGroup;
	}

	public static ProblemObject readDir(IPath absPath, String relPath) {
		initBuilder();
		File xmlFile = absPath.append("acme.xml").toFile();
		try {
			Document doc = docBuilder.parse(xmlFile);
			Node node = doc.getElementsByTagName("directory").item(0);
			String dirType = node.getAttributes().getNamedItem("type").getTextContent();
			if (dirType.equals("group")) {
				return readGroup(node, absPath, relPath);
			} else if (dirType.equals("problem")) {
				return readProblem(node, absPath, relPath);
			}
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public static void writeRoot() {
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		writeDir(ContestManager.getRoot(), workspaceRoot.getLocation());
	}

	public static void writeDir(ProblemObject problem, IPath absPath) {
		initBuilder();
		File xmlFile = absPath.append("acme.xml").toFile();
		absPath.toFile().mkdirs();
		try {
			xmlFile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Document doc = docBuilder.newDocument();
		Element root = doc.createElement("directory");
		doc.appendChild(root);
		if (problem instanceof ProblemGroup) {
			writeGroup(doc, root, (ProblemGroup) problem);
			for (ProblemObject child : ((ProblemGroup) problem).getChildren()) {
				writeDir(child, absPath.append(child.getRelPath()));
			}
		} else {
			writeProblem(doc, root, problem);
		}
		try {
			transformer.transform(new DOMSource(doc), new StreamResult(xmlFile));
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void writeGroup(Document doc, Element root, ProblemGroup group) {
		root.setAttribute("type", "group");
		root.setAttribute("name", group.getName());
		for (ProblemObject i : group.getChildren()) {
			Element child = doc.createElement("child");
			child.setAttribute("path", i.getRelPath());
			root.appendChild(child);
		}
	}

	private static void writeProblem(Document doc, Element root, ProblemObject problem) {
		root.setAttribute("type", "problem");
		root.setAttribute("name", problem.getName());
	}

	private static ProblemGroup readGroup(Node node, IPath absPath, String relPath) {
		ProblemGroup group = new ProblemGroup(node.getAttributes().getNamedItem("name").getTextContent());
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); ++i) {
			Node child = children.item(i);
			if (child.getNodeName() == "child") {
				String subpath = child.getAttributes().getNamedItem("path").getTextContent();
				group.addChild(readDir(absPath.append(subpath), subpath));
			}
		}
		group.setRelPath(relPath);
		group.setAbsPath(absPath);
		return group;
	}

	private static ProblemObject readProblem(Node node, IPath absPath, String relPath) {
		ProblemObject problem = new ProblemObject(node.getAttributes().getNamedItem("name").getTextContent());
		problem.setRelPath(relPath);
		problem.setAbsPath(absPath);
		return problem;
	}

}
