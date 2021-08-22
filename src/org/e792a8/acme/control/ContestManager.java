package org.e792a8.acme.control;

import org.e792a8.acme.control.problems.ProblemGroup;
import org.e792a8.acme.control.problems.ProblemObject;

public class ContestManager {

	private static ProblemGroup rootProblemGroup = ContestParser.parseRoot();
	private static ProblemObject currentProblem;

	public static void openProblem(ProblemObject problemObj) {
		currentProblem = problemObj;
	}

	public static ProblemObject getCurrentProblem() {
		return currentProblem;
	}

	public static ProblemGroup getRoot() {
		return rootProblemGroup;
	}

	public static ProblemGroup addGroup(ProblemGroup parent, String name) {
		ProblemGroup group = new ProblemGroup(name);
		parent.addChild(group);
		return group;
	}

	public static ProblemGroup addGroup(ProblemGroup parent) {
		return addGroup(parent, "Group " + (parent.getChildren().length + 1));
	}

	public static ProblemObject addProblem(ProblemGroup parent, String name) {
		ProblemObject obj = new ProblemObject(name);
		parent.addChild(obj);
		return obj;
	}

	public static ProblemObject addProblem(ProblemGroup parent) {
		return addProblem(parent, "Problem " + (parent.getChildren().length + 1));
	}

}
