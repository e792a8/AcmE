package org.e792a8.acme.core.runner.pipeline;

import org.e792a8.acme.core.runner.TestResult;
import org.e792a8.acme.core.workspace.SolutionConfig;

public abstract class APreprocessor implements Runnable {
	protected SolutionConfig solutionConfig;
	private TestResult testResult;
	private boolean finished;

	public APreprocessor(SolutionConfig solConf) {
		solutionConfig = solConf;
		finished = false;
	}

	/**
	 * Call this when the preprocess finishes
	 * 
	 * @param result resultCode should be "AC" if not "CE" or "SYSTEM ERROR"
	 */
	protected final void finish(TestResult result) {
		testResult = result;
		finished = true;
	}

	public final boolean isFinished() {
		return finished;
	}

	public final TestResult getResult() {
		if (isFinished())
			return testResult;
		return null;
	}

}
