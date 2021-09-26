package org.e792a8.acme.core.web.parse;

import org.e792a8.acme.core.web.parse.internal.CodeforcesContestParser;
import org.e792a8.acme.core.web.parse.internal.VjudgeContestParser;

public class ContestParserFactory {
	public static IContestParser createContestParser(String url) {
		// TODO detect oj type
		if (url.contains("vjudge")) {
			return createContestParser(url, "vjudge");
		}
		if (url.contains("codeforces")) {
			return createContestParser(url, "codeforces");
		}
		return null;
	}

	public static IContestParser createContestParser(String url, String oj) {
		// TODO oj support
		if ("vjudge".equals(oj)) {
			return new VjudgeContestParser(url);
		}
		if ("codeforces".equals(oj)) {
			return new CodeforcesContestParser(url);
		}
		return null;
	}
}
