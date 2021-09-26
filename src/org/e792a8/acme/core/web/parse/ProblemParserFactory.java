package org.e792a8.acme.core.web.parse;

import org.e792a8.acme.core.web.parse.internal.CodeforcesProblemParser;
import org.e792a8.acme.core.web.parse.internal.VjudgeProblemParser;

public class ProblemParserFactory {

	public static IProblemParser createProblemParser(String url) {
		// TODO detect oj type
		if (url.contains("vjudge")) {
			return createProblemParser(url, "vjudge");
		}
		if (url.contains("codeforces")) {
			return createProblemParser(url, "codeforces");
		}
		return null;
	}

	public static IProblemParser createProblemParser(String url, String oj) {
		// TODO oj support
		if ("vjudge".equals(oj)) {
			return new VjudgeProblemParser(url);
		}
		if ("codeforces".equals(oj)) {
			return new CodeforcesProblemParser(url);
		}
		return null;
	}

}
