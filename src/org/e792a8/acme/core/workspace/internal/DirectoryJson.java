package org.e792a8.acme.core.workspace.internal;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

class DirectoryJson {
	@Expose(deserialize = false, serialize = false)
	static Gson gson = new Gson();
	String type;
	String name;
	String url;
	List<String> children;
	JudgeJson judge;
	SolutionJson solution;
	List<TestJson> tests;

	static class JudgeJson {
		String type;
		String args;
	}

	static class SolutionJson {
		String lang;
		String path;
	}

	static class TestJson {
		String in;
		String ans;
	}
}
