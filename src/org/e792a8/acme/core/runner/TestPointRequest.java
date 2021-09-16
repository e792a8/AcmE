package org.e792a8.acme.core.runner;

import org.e792a8.acme.core.workspace.TestPointConfig;

public class TestPointRequest {
	protected TestPointConfig testPoint;
	protected IRunnerCallback callback;

	public TestPointRequest(TestPointConfig config, IRunnerCallback callback) {
		testPoint = config;
		this.callback = callback;
	}

	public TestPointConfig getTestPoint() {
		return testPoint;
	}

	public IRunnerCallback getCallback() {
		return callback;
	}
}
