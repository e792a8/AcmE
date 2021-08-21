package org.e792a8.acme.control.problems;

import java.util.LinkedList;

public class ProblemGroup extends ProblemObject {
	private LinkedList<ProblemObject> children;

	public ProblemGroup(String name) {
		super(name);
		children = new LinkedList<>();
	}

	public void addChild(ProblemObject child) {
		children.add(child);
		child.setParent(this);
	}

	public void removeChild(ProblemObject child) {
		children.remove(child);
		child.setParent(null);
	}

	public ProblemObject[] getChildren() {
		return children.toArray(new ProblemObject[children.size()]);
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	@Override
	public void dispose() {
		for (ProblemObject c : children) {
			c.dispose();
		}
		super.dispose();
		children.clear();
	}
}
