package org.e792a8.acme.core.runner.pipeline;

import java.io.File;

import org.e792a8.acme.core.runner.TestResult;
import org.e792a8.acme.core.workspace.TestPointConfig;

abstract class ATestRunner implements Runnable {
	private TestResult testResult;
	private boolean finished;
	protected TestPointConfig testPointConfig;
	protected File inputFile, outputFile, answerFile;

	public ATestRunner(TestPointConfig testConf) {
		testPointConfig = testConf;
		finished = false;
		inputFile = testConf.dirPath.append(testConf.in).toFile();
		answerFile = testConf.dirPath.append(testConf.ans).toFile();
	}

	/**
	 * Call this when the process finishes
	 * 
	 * @param result resultCode should be "AC" if not "RE" or "SYSTEM ERROR"
	 */
	protected final void finish(TestResult result, File output) {
		testResult = result;
		outputFile = output;
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
			return inputFile;
		return null;
	}

	public final File getOutput() {
		if (isFinished())
			return outputFile;
		return null;
	}

	public final File getAnswer() {
		if (isFinished())
			return answerFile;
		return null;
	}

}
