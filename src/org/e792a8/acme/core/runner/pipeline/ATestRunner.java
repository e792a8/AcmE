package org.e792a8.acme.core.runner.pipeline;

import java.io.File;

import org.e792a8.acme.core.runner.TestResult;
import org.e792a8.acme.core.workspace.ITestPoint;

abstract class ATestRunner implements Runnable {
	private TestResult testResult;
	private boolean finished;
	protected ITestPoint testPoint;
	protected File outputFile;

	public ATestRunner(ITestPoint testPoint) {
		this.testPoint = testPoint;
		finished = false;
	}

	public ITestPoint getTestPoint() {
		return testPoint;
	}

	/**
	 * Call this when the process finishes
	 * 
	 * @param result resultCode should be "AC" if not "RE" or "SYSTEM ERROR"
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

	public final File getInput() {
		if (isFinished())
			return getTestPoint().getInput().getFile();
		return null;
	}

	public final File getOutput() {
		if (isFinished())
			return testResult.outputFile;
		return null;
	}

	public final File getAnswer() {
		if (isFinished())
			return getTestPoint().getAnswer().getFile();
		return null;
	}

	public abstract void kill();

}
