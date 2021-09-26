package org.e792a8.acme.core.web;

import org.e792a8.acme.core.web.internal.VjudgeContestParser;

public class ContestParserFactory {
	public static IContestParser createContestParser(String url) {
		// TODO detect oj type
		return createContestParser(url, "vjudge");
	}

	public static IContestParser createContestParser(String url, String oj) {
		// TODO oj support
		if ("vjudge".equals(oj)) {
			return new VjudgeContestParser(url);
		}
		return null;
	}
}
