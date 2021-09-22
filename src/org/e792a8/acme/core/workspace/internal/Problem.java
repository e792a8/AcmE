package org.e792a8.acme.core.workspace.internal;

import java.util.LinkedList;
import java.util.List;

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
		// TODO get judge config
		return null;
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
		// TODO add testpoint
		return null;
	}

}
