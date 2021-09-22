package org.e792a8.acme.core.workspace;

import org.e792a8.acme.core.runner.judge.AJudge;

public interface IJudgeConfig extends IProblemElement {
	String getJudgeType();

	AJudge getJudge();
}
