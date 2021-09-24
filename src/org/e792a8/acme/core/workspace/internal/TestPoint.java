package org.e792a8.acme.core.workspace.internal;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.IProblem;
import org.e792a8.acme.core.workspace.ITestPoint;
import org.e792a8.acme.core.workspace.ITestPointAnswer;
import org.e792a8.acme.core.workspace.ITestPointInput;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TestPoint implements ITestPoint {

	private IProblem problem;
	private String in;
	private String ans;

	private class TestPointInput extends TestPointElement implements ITestPointInput {

		TestPointInput(ITestPoint testPoint, String fileName) {
			super(testPoint, fileName);
		}

	}

	private class TestPointAnswer extends TestPointElement implements ITestPointAnswer {

		TestPointAnswer(ITestPoint testPoint, String fileName) {
			super(testPoint, fileName);
		}

	}

	TestPoint(IProblem problem, String in, String ans) {
		this.problem = problem;
		this.in = in;
		this.ans = ans;
	}

	@Override
	public IProblem getProblem() {
		return problem;
	}

	@Override
	public void delete() {
		Document doc = ConfigParser.readDoc(getDirectory().getLocation());
		NodeList nl = doc.getElementsByTagName("test");
		for (int i = 0; i < nl.getLength(); ++i) {
			Element e = (Element) nl.item(i);
			if (getInput().getFileName().equals(e.getAttribute("in"))
				&& getAnswer().getFileName().equals(e.getAttribute("ans"))) {
				getInput().getFile().delete();
				getAnswer().getFile().delete();
				doc.getDocumentElement().removeChild(e);
				ConfigParser.writeDoc(doc, getDirectory().getLocation());
				return;
			}
		}
	}

	@Override
	public boolean isValid() {
		return getInput().isValid() && getAnswer().isValid();
	}

	@Override
	public IDirectory getDirectory() {
		return getProblem();
	}

	@Override
	public ITestPointInput getInput() {
		return new TestPointInput(this, in);
	}

	@Override
	public ITestPointAnswer getAnswer() {
		return new TestPointAnswer(this, ans);
	}

}
