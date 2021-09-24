package org.e792a8.acme.core.workspace.internal;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.e792a8.acme.core.workspace.IGroup;
import org.e792a8.acme.core.workspace.IJudgeConfig;
import org.e792a8.acme.core.workspace.IProblem;
import org.e792a8.acme.core.workspace.ISolution;
import org.e792a8.acme.core.workspace.ITestPoint;
import org.eclipse.core.runtime.IPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Problem extends Directory implements IProblem {

	public Problem(IGroup parent, IPath fullPath) {
		super(parent, fullPath);
	}

	public Problem(IPath fullPath) {
		super(fullPath);
	}

	@Override
	public IJudgeConfig getJudgeConfig() {
		return new JudgeConfig(this);
	}

	@Override
	public ISolution getSolution() {
		Document doc = ConfigParser.readDoc(getLocation());
		Element solElem = (Element) doc.getElementsByTagName("solution").item(0);
		return new Solution(this, solElem.getAttribute("lang"), solElem.getAttribute("path"));
	}

	@Override
	public List<ITestPoint> getTestPoints() {
		Document doc = ConfigParser.readDoc(getLocation());
		NodeList nl = doc.getElementsByTagName("test");
		List<ITestPoint> ls = new LinkedList<>();
		for (int i = 0; i < nl.getLength(); ++i) {
			Element e = (Element) nl.item(i);
			ls.add(new TestPoint(this, e.getAttribute("in"), e.getAttribute("ans")));
		}
		return ls;
	}

	@Override
	public ITestPoint addTestPoint() {
		Document doc = ConfigParser.readDoc(getLocation());
		NodeList nl = doc.getElementsByTagName("test");
		Set<String> names = new TreeSet<>();
		for (int i = 0; i < nl.getLength(); ++i) {
			Element e = (Element) nl.item(i);
			names.add(e.getAttribute("in"));
			names.add(e.getAttribute("ans"));
		}
		int i = 0;
		String iname, aname;
		do {
			++i;
			iname = "in" + i + ".txt";
			aname = "ans" + i + ".txt";
		} while (names.contains(iname) || names.contains(aname));
		Element ne = doc.createElement("test");
		ne.setAttribute("in", iname);
		ne.setAttribute("ans", aname);
		doc.getDocumentElement().appendChild(ne);
		getLocation().append(iname).toFile().delete();
		try {
			getLocation().append(iname).toFile().createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getLocation().append(aname).toFile().delete();
		try {
			getLocation().append(aname).toFile().createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ConfigParser.writeDoc(doc, getLocation());
		return new TestPoint(this, iname, aname);
	}

}
