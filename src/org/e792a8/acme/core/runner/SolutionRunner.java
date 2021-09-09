package org.e792a8.acme.core.runner;

import java.util.LinkedList;
import java.util.List;

import org.e792a8.acme.core.workspace.DirectoryConfig;
import org.e792a8.acme.core.workspace.SolutionConfig;
import org.e792a8.acme.core.workspace.TestPointConfig;

/**
 * 
 * The runner of a solution
 * 
 * Responsipble for choosing the proper pipeline and judge
 *
 */
public class SolutionRunner {
	private DirectoryConfig directoryConfig;
	private SolutionConfig solutionConfig;
	private List<TestPointConfig> testPointConfigs;
	private List<TestResult> testResults;

	public SolutionRunner(DirectoryConfig dirConf, SolutionConfig solConf, TestPointConfig tpConf) {
		List<TestPointConfig> confs = new LinkedList<>();
		confs.add(tpConf);
		directoryConfig = dirConf;
		solutionConfig = solConf;
		testPointConfigs = confs;
		initialize();
	}

	public SolutionRunner(DirectoryConfig dirConf, SolutionConfig solConf, List<TestPointConfig> tpConfs) {
		directoryConfig = dirConf;
		solutionConfig = solConf;
		testPointConfigs = tpConfs;
		initialize();
	}

	private void initialize() {

	}

	public boolean run() {
		// TODO check language and judge type, and launch a runner pipeline
		return false;
	}
}
