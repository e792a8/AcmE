package org.e792a8.acme.core.runner;

import java.util.ArrayList;
import java.util.List;

import org.e792a8.acme.core.runner.judge.AJudge;
import org.e792a8.acme.core.runner.judge.StrictJudge;
import org.e792a8.acme.core.runner.pipeline.ARunner;
import org.e792a8.acme.core.runner.pipeline.CppRunner;
import org.e792a8.acme.core.workspace.IJudgeConfig;
import org.e792a8.acme.core.workspace.ISolution;

/**
 * 
 * The runner factory of a solution
 * 
 * Responsipble for choosing the proper pipeline and judge
 *
 */
public class RunnerFactory {
	public static final ARunner createRunner(ISolution solConf, TestPointRequest request,
		IRunnerCallback mainCallback) {
		List<TestPointRequest> req = new ArrayList<>();
		req.add(request);
		return createRunner(solConf, req, mainCallback);
	}

	public static final ARunner createRunner(ISolution solConf, List<TestPointRequest> requests,
		IRunnerCallback mainCallback) {
		AJudge judge = createJudge(solConf.getProblem().getJudgeConfig());
		return createPipeline(solConf, requests, judge, mainCallback);
	}

	/**
	 * Extend this to support more types of judge
	 * 
	 * @param config the JudgeConfig
	 * @return
	 */
	private static final AJudge createJudge(IJudgeConfig config) {
		String type = config.getJudgeType();
		if ("strict".equals(type)) {
			return new StrictJudge();
		}
		return null;
	}

	/**
	 * Extend this to support more languages
	 * 
	 * @param solution the SolutionConfig
	 * @param judge    the judge to use in the pipeline
	 * @return
	 */
	public static final ARunner createPipeline(ISolution solution, List<TestPointRequest> requests,
		AJudge judge, IRunnerCallback mainCallback) {
		String lang = solution.getLang();
		if ("cpp".equals(lang)) {
			return new CppRunner(solution, requests, judge, mainCallback);
		}
		return null;
	}
}
