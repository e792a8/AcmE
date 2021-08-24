package org.e792a8.acme.control.problems;

import org.eclipse.core.runtime.IPath;

public class ProblemObject {
	private String name;
	private ProblemGroup parent;
	private String relPath;
	private IPath absPath;

	public ProblemObject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public IPath getAbsPath() {
		return absPath;
	}

	public void setAbsPath(IPath path) {
		this.absPath = path;
	}

	public String getRelPath() {
		return relPath;
	}

	public void setRelPath(String path) {
		this.relPath = path;
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
