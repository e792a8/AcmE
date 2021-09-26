package org.e792a8.acme.core.web.parse.internal;

import java.util.Iterator;

import org.e792a8.acme.core.web.parse.IProblemParser;
import org.e792a8.acme.core.workspace.IProblem;
import org.e792a8.acme.core.workspace.ITestPoint;
import org.e792a8.acme.utils.FileSystem;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CodeforcesProblemParser extends WebParser implements IProblemParser {

	public CodeforcesProblemParser(String url) {
		super(url);
	}

	@Override
	public IProblem parseTo(IProblem problem) {
		Document doc = readDocument();
		if (doc == null) {
			return null;
		}
		Elements ins = doc.getElementsByClass("input");
		Elements ans = doc.getElementsByClass("output");
		if (ins.size() != ans.size()) {
			return null;
		}
		Iterator<Element> iti = ins.iterator(), ita = ans.iterator();
		while (iti.hasNext() && ita.hasNext()) {
			String input = iti.next().getElementsByTag("pre").get(0).text();
			String answer = ita.next().getElementsByTag("pre").get(0).text();
			ITestPoint tp = problem.addTestPoint();
			FileSystem.write(tp.getInput().getFile(), input);
			FileSystem.write(tp.getAnswer().getFile(), answer);
		}
		return problem;
	}

}
