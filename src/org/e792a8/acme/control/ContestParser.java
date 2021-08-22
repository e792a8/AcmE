package org.e792a8.acme.control;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.e792a8.acme.control.problems.ProblemGroup;
import org.e792a8.acme.control.problems.ProblemObject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ContestParser {

	private static DocumentBuilder docBuilder;

	public static ProblemGroup parseRoot() {
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		return (ProblemGroup) parseDir(workspaceRoot.getLocation());
	}

	public static ProblemObject parseDir(IPath path) {
		File xmlFile = path.append("acme.xml").toFile();
		if (docBuilder == null) {
			try {
				docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		try {
			Document doc = docBuilder.parse(xmlFile);
			Node node = doc.getElementsByTagName("directory").item(0);
			String dirType = node.getAttributes().getNamedItem("type").getTextContent();
			if (dirType.equals("group")) {
				return parseGroup(node, path);
			} else if (dirType.equals("problem")) {
				return parseProblem(node, path);
			}
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}

	private static ProblemGroup parseGroup(Node node, IPath path) {
		ProblemGroup group = new ProblemGroup(node.getAttributes().getNamedItem("name").getTextContent());
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); ++i) {
			Node child = children.item(i);
			if (child.getNodeName() == "child") {
				String subpath = child.getAttributes().getNamedItem("path").getTextContent();
				group.addChild(parseDir(path.append(subpath)));
			}
		}
		return group;
	}

	private static ProblemObject parseProblem(Node node, IPath path) {
		ProblemObject problem = new ProblemObject(node.getAttributes().getNamedItem("name").getTextContent());
		return problem;
	}

}
