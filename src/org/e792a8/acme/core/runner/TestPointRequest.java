package org.e792a8.acme.core.runner;

import org.e792a8.acme.core.workspace.ITestPoint;

public class TestPointRequest {
	protected ITestPoint testPoint;
	protected IRunnerCallback callback;

	public TestPointRequest(ITestPoint testPoint, IRunnerCallback callback) {
		this.testPoint = testPoint;
		this.callback = callback;
	}

	public ITestPoint getTestPoint() {
		return testPoint;
	}

	public IRunnerCallback getCallback() {
		return callback;
	}
}
