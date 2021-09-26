package org.e792a8.acme.core.web.internal;

import org.e792a8.acme.core.web.IProblemParser;
import org.e792a8.acme.core.workspace.IProblem;

public class VjudgeProblemParser extends WebParser implements IProblemParser {

	public VjudgeProblemParser(String url) {
		super(url);
	}

	@Override
	public IProblem parseTo(IProblem problem) {
		// TODO vjudge problem parser
		return problem;
	}

}
