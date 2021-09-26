package org.e792a8.acme.core.web.parse.internal;

import java.net.MalformedURLException;
import java.net.URL;

import org.e792a8.acme.core.web.parse.IContestParser;
import org.e792a8.acme.core.workspace.IGroup;
import org.e792a8.acme.core.workspace.IProblem;
import org.e792a8.acme.utils.FileSystem;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CodeforcesContestParser extends WebParser implements IContestParser {

	public CodeforcesContestParser(String url) {
		super(url);
	}

	@Override
	public IGroup parseTo(IGroup group) {
		Document doc = readDocument();
		if (doc == null) {
			return null;
		}
		Element probt = doc.getElementsByClass("problems").get(0);
		Elements trs = probt.getElementsByTag("tr");
		trs.remove(0);
		String urlBase = getUrl();
		try {
			urlBase = getUrl().replaceAll(new URL(getUrl()).getPath(), "");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (Element tr : trs) {
			String num = tr.getAllElements().get(2).text();
			Element e = tr.getAllElements().get(6);
			String pname = num + ". " + e.text();
			String purl = urlBase + e.attr("href");
			IProblem problem = group.createSubDirectory().setType("problem").setName(pname).setUrl(purl)
				.setFileName(FileSystem.safePathName(pname)).finish().toProblem();
			new CodeforcesProblemParser(purl).parseTo(problem);
		}
		return group;
	}

}
