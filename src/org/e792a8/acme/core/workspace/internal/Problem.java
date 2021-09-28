package org.e792a8.acme.core.workspace.internal;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.e792a8.acme.core.workspace.IJudgeConfig;
import org.e792a8.acme.core.workspace.IProblem;
import org.e792a8.acme.core.workspace.ISolution;
import org.e792a8.acme.core.workspace.ITestPoint;
import org.e792a8.acme.core.workspace.internal.DirectoryJson.TestJson;
import org.eclipse.core.runtime.IPath;

public class Problem extends Directory implements IProblem {

	public Problem(IPath fullPath, String fileName) {
		super(fullPath, fileName);
	}

	@Override
	public IJudgeConfig getJudgeConfig() {
		return new JudgeConfig(this);
	}

	@Override
	public ISolution getSolution() {
		DirectoryJson.SolutionJson json = null;
		try {
			json = getJson().solution;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return new Solution(this, json.lang, json.path);
	}

	@Override
	public List<ITestPoint> getTestPoints() {
		List<TestJson> json;
		try {
			json = getJson().tests;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		List<ITestPoint> ls = new LinkedList<>();
		for (TestJson t : json) {
			ls.add(new TestPoint(this, t.in, t.ans));
		}
		return ls;
	}

	@Override
	public boolean isValid() {
		try {
			return super.isValid() && "problem".equals(getJson().type);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public ITestPoint addTestPoint() throws IOException {
		DirectoryJson json;
		try {
			json = getJson();
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
		Set<String> names = new TreeSet<>();
		for (TestJson t : json.tests) {
			names.add(t.in);
			names.add(t.ans);
		}
		int i = 0;
		String iname, aname;
		do {
			++i;
			iname = "in" + i + ".txt";
			aname = "ans" + i + ".txt";
		} while (names.contains(iname) || names.contains(aname));
		TestJson tj = new TestJson();
		tj.in = iname;
		tj.ans = aname;
		json.tests.add(tj);
		File ifile = getLocation().append(iname).toFile();
		File afile = getLocation().append(aname).toFile();
		try {
			ifile.delete();
			ifile.createNewFile();
		} catch (IOException e) {
			ifile.delete();
			throw e;
		}
		try {
			afile.delete();
			afile.createNewFile();
		} catch (IOException e) {
			afile.delete();
			ifile.delete();
			throw e;
		}
		JsonParser.writeJson(getFullPath(), json);
		return new TestPoint(this, iname, aname);
	}

}
