package org.e792a8.acme.core.runner.pipeline;

import java.util.LinkedList;
import java.util.List;

import org.e792a8.acme.core.runner.IRunnerCallback;
import org.e792a8.acme.core.runner.TestPointRequest;
import org.e792a8.acme.core.runner.TestResult;
import org.e792a8.acme.core.runner.judge.AJudge;
import org.e792a8.acme.core.workspace.SolutionConfig;
import org.e792a8.acme.core.workspace.TestPointConfig;

/**
 * 
 * The abstract pipeline for running and judging a solution with specified test
 * points and judge
 * 
 * Well this might be better named as APipeline.
 *
 */
public abstract class ARunner {
	private SolutionConfig solutionConfig;
	private AJudge judge;
	private APreprocessor preprocessor;
	private List<TestRunnerThread> runnerThreads;
	private TestResult mainTestResult;
	private boolean finished;
	private MainRunnerThread monitorThread;
	private IRunnerCallback mainCallback;

	private class TestRunnerThread extends Thread {

		protected ATestRunner runner;
		private AJudge judge;
		private TestResult testResult;
		private boolean finished;
		protected IRunnerCallback callback;

		public TestRunnerThread(ATestRunner runner, AJudge judge, IRunnerCallback callback) {
			this.runner = runner;
			this.judge = judge;
			this.callback = callback;
			testResult = null;
			finished = false;
		}

		private void finish(TestResult res) {
			testResult = res;
			finished = true;
			if (callback != null)
				callback.finish(res);
		}

		public boolean isFinished() {
			return finished;
		}

		public TestResult getResult() {
			return testResult;
		}

		@Override
		public void run() {
			TestResult res = null;
			runner.run();
			if (callback != null)
				callback.start();
			if (!runner.isFinished()) {
				res = new TestResult();
				res.resultCode = "CE";
				res.message = "Failed to get result from runner";
				finish(res);
				return;
			}
			res = runner.getResult();
			if (!"AC".equals(res.resultCode)) {
				finish(res);
				return;
			}
			TestResult jres = judge.judge(runner.getInput(), runner.getOutput(), runner.getAnswer());
			jres.outputFile = res.outputFile;
			finish(jres);
			return;
		}

		@Override
		public void interrupt() {
			if (runner != null)
				runner.kill();
			super.interrupt();
		}
	}

	private class MainRunnerThread extends Thread {

		@Override
		public void run() {
			preprocessor.run();
			TestResult res = preprocessor.getResult();
			if (!"AC".equals(res.resultCode)) {
				for (TestRunnerThread t : runnerThreads) {
					if (t.callback != null)
						t.callback.finish(null);
				}
				finish(res);
				return;
			}
			if (mainCallback != null)
				mainCallback.start();
			for (TestRunnerThread t : runnerThreads) {
				t.start();
			}
			boolean allFinished = false;
			while (!allFinished) {
				boolean notFinished = false;
				for (TestRunnerThread t : runnerThreads) {
					try {
						t.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					}
					if (!t.isFinished()) {
						notFinished = true;
						break;
					}
				}
				if (!notFinished) {
					allFinished = true;
				}
			}
			finish(checkResult());
		}
	}

	public ARunner(SolutionConfig solConf, List<TestPointRequest> requests, AJudge judge,
		IRunnerCallback mainCallback) {
		solutionConfig = solConf;
		this.mainCallback = mainCallback;
		this.preprocessor = createPreprocessor(solConf);
		this.judge = judge;
		finished = false;
		initialize(requests);
	}

	private final void initialize(List<TestPointRequest> requests) {
		preprocessor = createPreprocessor(solutionConfig);
		runnerThreads = new LinkedList<>();
		for (TestPointRequest req : requests) {
			runnerThreads.add(new TestRunnerThread(createTestRunner(req.getTestPoint()), judge, req.getCallback()));
		}
		monitorThread = new MainRunnerThread();
	}

	private final TestResult checkResult() {
		TestResult res = null;
		for (TestRunnerThread t : runnerThreads) {
			res = t.getResult();
			if (!"AC".equals(res.resultCode)) {
				return res;
			}
		}
		TestResult r = new TestResult();
		r.resultCode = "AC";
		r.message = "Passed all test points";
		return r;
	}

	/**
	 * Call this when the pipeline finishes
	 * 
	 * @param mainResult
	 * @param results
	 */
	protected final void finish(TestResult mainResult) {
		mainTestResult = mainResult;
		finished = true;
		if (mainCallback != null)
			mainCallback.finish(mainResult);
	}

	public final boolean isFinished() {
		return finished;
	}

	public final TestResult getMainResult() {
		if (isFinished())
			return mainTestResult;
		return null;
	}

	public final List<TestResult> getResults() {
		if (!isFinished()) {
			return null;
		}
		List<TestResult> results = new LinkedList<>();
		for (TestRunnerThread t : runnerThreads) {
			results.add(t.getResult());
		}
		return results;
	}

	public final boolean launch() {
		monitorThread.start();
		return true;
	}

	public final boolean terminate() {
		if (!isFinished()) {
			monitorThread.interrupt();
			for (TestRunnerThread t : runnerThreads) {
				t.interrupt();
			}
		}
		return true;
	}

	protected abstract APreprocessor createPreprocessor(SolutionConfig solConfig);

	protected abstract ATestRunner createTestRunner(TestPointConfig testConfig);
}
