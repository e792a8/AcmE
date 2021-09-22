package org.e792a8.acme.core.workspace;

import java.util.List;

public interface IProblem extends IDirectory {
	IJudgeConfig getJudgeConfig();

	ISolution getSolution();

	List<ITestPoint> getTestPoints();

	ITestPoint addTestPoint();
}
