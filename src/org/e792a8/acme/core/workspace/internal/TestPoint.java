package org.e792a8.acme.core.workspace.internal;

import java.io.IOException;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.IProblem;
import org.e792a8.acme.core.workspace.ITestPoint;
import org.e792a8.acme.core.workspace.ITestPointAnswer;
import org.e792a8.acme.core.workspace.ITestPointInput;
import org.e792a8.acme.core.workspace.IWorkspaceElement;
import org.e792a8.acme.core.workspace.internal.DirectoryJson.TestJson;

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
	public void delete() throws IOException {
		DirectoryJson json = ((Problem) getProblem()).getJson();
		for (TestJson e : json.tests) {
			if (getInput().getFileName().equals(e.in)
				&& getAnswer().getFileName().equals(e.ans)) {
				getInput().delete();
				getAnswer().delete();
				json.tests.remove(e);
				JsonParser.writeJson(getProblem().getFullPath(), json);
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

	@Override
	public int compareTo(IWorkspaceElement arg0) {
		int res = getProblem().compareTo(arg0);
		if (res == 0) {
			res = getInput().getFileName().compareTo(((ITestPoint) arg0).getInput().getFileName());
		}
		if (res == 0) {
			res = getAnswer().getFileName().compareTo(((ITestPoint) arg0).getAnswer().getFileName());
		}
		return res;
	}

}
