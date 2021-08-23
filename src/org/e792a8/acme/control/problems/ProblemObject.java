package org.e792a8.acme.control.problems;

public class ProblemObject {
	private String name;
	private ProblemGroup parent;
	private String path;

	public ProblemObject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setParent(ProblemGroup parent) {
		this.parent = parent;
	}

	public ProblemGroup getParent() {
		return parent;
	}

	public void dispose() {

	}

	@Override
	public String toString() {
		return name;
	}
}
