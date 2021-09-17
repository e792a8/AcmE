package org.e792a8.acme.core.runner.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.e792a8.acme.core.runner.IRunnerCallback;
import org.e792a8.acme.core.runner.TestPointRequest;
import org.e792a8.acme.core.runner.TestResult;
import org.e792a8.acme.core.runner.judge.AJudge;
import org.e792a8.acme.core.workspace.SolutionConfig;
import org.e792a8.acme.core.workspace.TestPointConfig;
import org.e792a8.acme.utils.FileSystem;

public class CppRunner extends ARunner {

	public CppRunner(SolutionConfig solConf, List<TestPointRequest> requests, AJudge judge,
		IRunnerCallback mainCallback) {
		super(solConf, requests, judge, mainCallback);
	}

	private String executable;

	private class CppPreprocessor extends APreprocessor {

		public CppPreprocessor(SolutionConfig solConf) {
			super(solConf);
		}

		@Override
		public void run() {
			TestResult res = new TestResult();
			Runtime rt = Runtime.getRuntime();
			String sourcePath = solutionConfig.directory.absPath.append(solutionConfig.path).toOSString();
			String executablePath = FileSystem.createTempDir() + File.separator + "sol.exe";
			String[] compileCmd = {
				"g++",
				sourcePath,
				"-o",
				executablePath,
			};
			try {
				Process compileProcess = rt.exec(compileCmd);
				int compileRet = compileProcess.waitFor();
				if (compileRet != 0) {
					res.resultCode = "CE";
					res.message = "Compiler return code: " + compileRet;
					finish(res);
					return;
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				res.resultCode = "SYSTEM ERROR";
				res.message = e.getMessage();
				finish(res);
				return;
			}
			res.resultCode = "AC";
			executable = executablePath;
			finish(res);
		}

	}

	private class CppTestRunner extends ATestRunner {

		private Process runProcess = null;

		public CppTestRunner(TestPointConfig testConf) {
			super(testConf);
		}

		@Override
		public void run() {
			long startTime = -1, duration = -1;
			String outputPath = FileSystem.createTempDir().getAbsolutePath() + File.separator + "out.txt";
			File outputFile = new File(outputPath);
			ProcessBuilder processBuilder = new ProcessBuilder().command(executable)
				.redirectInput(inputFile).redirectOutput(outputFile);
			int retCode;
			TestResult res = null;
			try {
				startTime = System.currentTimeMillis();
				runProcess = processBuilder.start();
				retCode = runProcess.waitFor();
				duration = System.currentTimeMillis() - startTime;
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				res = new TestResult();
				res.resultCode = "RE";
				res.message = e.getMessage();
				finish(res);
				return;
			}
			if (retCode != 0) {
				res = new TestResult();
				res.resultCode = "RE";
				res.message = "Return code: " + retCode;
				finish(res);
				return;
			}
			res = new TestResult();
			res.resultCode = "AC";
			res.timeMs = duration;
			res.memoryKb = -1; // TODO memory monitor
			res.outputFile = outputFile;
			finish(res);
			return;
		}

		@Override
		public void kill() {
			if (runProcess != null) {
				runProcess.destroy();
			}
		}

	}

	@Override
	protected APreprocessor createPreprocessor(SolutionConfig solConfig) {
		return new CppPreprocessor(solConfig);
	}

	@Override
	protected ATestRunner createTestRunner(TestPointConfig testConfig) {
		return new CppTestRunner(testConfig);
	}

}
