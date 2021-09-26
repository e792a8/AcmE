package org.e792a8.acme.core.web.parse.internal;

import org.e792a8.acme.core.web.parse.IContestParser;
import org.e792a8.acme.core.workspace.IGroup;
import org.e792a8.acme.core.workspace.IProblem;
import org.e792a8.acme.utils.FileSystem;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VjudgeContestParser extends WebParser implements IContestParser {

	public VjudgeContestParser(String url) {
		super(url);
	}

	@Override
	public IGroup parseTo(IGroup group) {
		Document doc = readDocument();
		if (doc == null) {
			return null;
		}
		Elements trs = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr");
		for (Element e : trs) {
			Elements tds = e.getElementsByTag("td");
			String pname = tds.get(2).text() + ". " + tds.get(3).getElementsByTag("a").text();
			String purl = getUrl() + tds.get(3).getElementsByTag("a").get(0).attr("href");
			IProblem problem = group.createSubDirectory().setType("problem").setName(pname).setUrl(purl)
				.setFileName(FileSystem.safePathName(pname)).finish().toProblem();
			new VjudgeProblemParser(purl).parseTo(problem);
		}
		return group;
	}

}
