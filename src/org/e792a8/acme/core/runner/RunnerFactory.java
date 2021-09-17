package org.e792a8.acme.core.runner;

import java.util.ArrayList;
import java.util.List;

import org.e792a8.acme.core.runner.judge.AJudge;
import org.e792a8.acme.core.runner.judge.StrictJudge;
import org.e792a8.acme.core.runner.pipeline.ARunner;
import org.e792a8.acme.core.runner.pipeline.CppRunner;
import org.e792a8.acme.core.workspace.JudgeConfig;
import org.e792a8.acme.core.workspace.SolutionConfig;

/**
 * 
 * The runner factory of a solution
 * 
 * Responsipble for choosing the proper pipeline and judge
 *
 */
public class RunnerFactory {
	public static final ARunner createRunner(SolutionConfig solConf, TestPointRequest request,
		IRunnerCallback mainCallback) {
		List<TestPointRequest> req = new ArrayList<>();
		req.add(request);
		return createRunner(solConf, req, mainCallback);
	}

	public static final ARunner createRunner(SolutionConfig solConf, List<TestPointRequest> requests,
		IRunnerCallback mainCallback) {
		AJudge judge = createJudge(solConf.directory.judge);
		return createPipeline(solConf, requests, judge, mainCallback);
	}

	/**
	 * Extend this to support more types of judge
	 * 
	 * @param config the JudgeConfig
	 * @return
	 */
	private static final AJudge createJudge(JudgeConfig config) {
		String type = config.type;
		if ("strict".equals(type)) {
			return new StrictJudge();
		}
		return null;
	}

	/**
	 * Extend this to support more languages
	 * 
	 * @param config the SolutionConfig
	 * @param judge  the judge to use in the pipeline
	 * @return
	 */
	public static final ARunner createPipeline(SolutionConfig config, List<TestPointRequest> requests,
		AJudge judge, IRunnerCallback mainCallback) {
		String lang = config.lang;
		if ("cpp".equals(lang)) {
			return new CppRunner(config, requests, judge, mainCallback);
		}
		return null;
	}
}
