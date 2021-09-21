package org.e792a8.acme.core.workspace;

import java.util.List;

public interface IProblem extends IDirectory {
	IJudge getJudge();

	ISolution getSolution();

	List<ITestPoint> getTestPoints();
}
