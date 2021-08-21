package org.e792a8.acme.control.problems;

public class ProblemObject {
	private String name;
	private ProblemGroup parent;

	public ProblemObject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
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
