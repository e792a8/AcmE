package org.e792a8.acme.core.runner.judge;

import java.io.File;

import org.e792a8.acme.core.runner.TestResult;

public abstract class AJudge {

	public abstract TestResult judge(File inputFile, File outputFile, File answerFile);

}
