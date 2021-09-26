package org.e792a8.acme.core.web;

import org.e792a8.acme.core.web.internal.VjudgeProblemParser;

public class ProblemParserFactory {

	public static IProblemParser createProblemParser(String url) {
		// TODO detect oj type
		return createProblemParser(url, "vjudge");
	}

	public static IProblemParser createProblemParser(String url, String oj) {
		// TODO oj support
		if ("vjudge".equals(oj)) {
			return new VjudgeProblemParser(url);
		}
		return null;
	}

}
