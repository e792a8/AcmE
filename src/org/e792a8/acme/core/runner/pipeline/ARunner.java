package org.e792a8.acme.core.runner.pipeline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	private List<TestPointConfig> testPointConfigs;
	private AJudge judge;
	private APreprocessor preprocessor;
	private List<RunnerThread> runnerThreads;
	private TestResult mainTestResult;
	private List<TestResult> testResults;
	private boolean finished;
	private MonitorThread monitorThread;

	private class RunnerThread extends Thread {

		protected ATestRunner runner;
		private AJudge judge;
		private TestResult testResult;
		private boolean finished;

		public RunnerThread(ATestRunner runner, AJudge judge) {
			this.runner = runner;
			this.judge = judge;
			testResult = null;
			finished = false;
		}

		private void finish(TestResult res) {
			testResult = res;
			finished = true;
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
			res = judge.judge(runner.getInput(), runner.getOutput(), runner.getAnswer());
			finish(res);
			return;
		}
	}

	private class MonitorThread extends Thread {
		@Override
		public void run() {
			boolean allFinished = false;
			while (!allFinished) {
				boolean notFinished = false;
				for (int i = 0; i < runnerThreads.size(); ++i) {
					if (!runnerThreads.get(i).isFinished()) {
						notFinished = true;
						continue;
					}
					TestResult res = runnerThreads.get(i).getResult();
					if (!"AC".equals(res.resultCode)) {
						testResults.set(i, res);
					} else {
						ATestRunner r = runnerThreads.get(i).runner;
						res = judge.judge(r.getInput(), r.getOutput(), r.getAnswer());
						testResults.set(i, res);
					}
				}
				if (!notFinished) {
					allFinished = true;
				}
			}
			finish(checkResult());
		}
	}

	public ARunner(SolutionConfig solConf, List<TestPointConfig> tpConfs, AJudge judge) {
		solutionConfig = solConf;
		testPointConfigs = tpConfs;
		this.judge = judge;
		finished = false;
		initialize();
	}

	private final void initialize() {
		preprocessor = createPreprocessor(solutionConfig);
		runnerThreads = new ArrayList<>(testPointConfigs.size());
		testResults = new ArrayList<>(testPointConfigs.size());
		monitorThread = new MonitorThread();
		Iterator<TestPointConfig> it1 = testPointConfigs.iterator();
		int i = 0;
		while (it1.hasNext()) {
			TestPointConfig config = it1.next();
			runnerThreads.set(i, new RunnerThread(createTestRunner(config), judge));
			++i;
		}
	}

	private final TestResult checkResult() {
		Iterator<TestResult> it = testResults.iterator();
		TestResult r = null;
		while (it.hasNext()) {
			TestResult res = it.next();
			if (!"AC".equals(res.resultCode)) {
				return res;
			}
		}
		r = new TestResult();
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
	}

	public final boolean isFinished() {
		return finished;
	}

	public final TestResult getMainResult() {
		if (finished)
			return mainTestResult;
		return null;
	}

	public final List<TestResult> getResults() {
		if (finished)
			return testResults;
		return null;
	}

	private final boolean preprocess() {
		preprocessor.run();
		TestResult result = null;
		if (preprocessor.isFinished()) {
			result = preprocessor.getResult();
		}
		if (result == null) {
			return false;
		}
		if (!"AC".equals(result.resultCode)) {
			finish(result);
			return false;
		}
		return true;
	}

	private final boolean run() {
		Iterator<RunnerThread> it = runnerThreads.iterator();
		while (it.hasNext()) {
			it.next().start();
		}
		monitorThread.start();
		return true;
	}

	public final boolean launch() {
		boolean res = false;
		res = preprocess();
		if (res)
			res = run();
		return true;
	}

	protected abstract APreprocessor createPreprocessor(SolutionConfig solConfig);

	protected abstract ATestRunner createTestRunner(TestPointConfig testConfig);
}
