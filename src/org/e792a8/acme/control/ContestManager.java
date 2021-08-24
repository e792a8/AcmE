package org.e792a8.acme.control;

import org.e792a8.acme.control.problems.ProblemGroup;
import org.e792a8.acme.control.problems.ProblemObject;

public class ContestManager {

	private static ProblemGroup rootProblemGroup = WorkspaceParser.readRoot();
	private static ProblemObject currentProblem;

	public static void openProblem(ProblemObject problemObj) {
		currentProblem = problemObj;
	}

	public static ProblemObject getCurrentProblem() {
		return currentProblem;
	}

	public static ProblemGroup getRoot() {
		if (rootProblemGroup.getRelPath() == null) {
			rootProblemGroup.setRelPath("./");
		}
		return rootProblemGroup;
	}

	public static ProblemGroup addGroup(ProblemGroup parent, String name) {
		ProblemGroup group = new ProblemGroup(name);
		group.setRelPath(name + "/");
		group.setAbsPath(parent.getAbsPath().append(group.getRelPath()));
		parent.addChild(group);
		WorkspaceParser.writeRoot();
		return group;
	}

	public static ProblemGroup addGroup(ProblemGroup parent) {
		return addGroup(parent, "Group " + (parent.getChildren().length + 1));
	}

	public static ProblemObject addProblem(ProblemGroup parent, String name) {
		ProblemObject obj = new ProblemObject(name);
		obj.setRelPath(name + "/");
		obj.setAbsPath(parent.getAbsPath().append(obj.getRelPath()));
		parent.addChild(obj);
		WorkspaceParser.writeRoot();
		return obj;
	}

	public static ProblemObject addProblem(ProblemGroup parent) {
		return addProblem(parent, "Problem " + (parent.getChildren().length + 1));
	}

}
