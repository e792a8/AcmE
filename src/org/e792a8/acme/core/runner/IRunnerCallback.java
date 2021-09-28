package org.e792a8.acme.core.runner;

public interface IRunnerCallback {
	public void start();

	public void finish(TestResult result);

	public void handleException(Exception e);

}
