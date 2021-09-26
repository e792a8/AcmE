package org.e792a8.acme.core.web.parse.internal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.e792a8.acme.core.web.parse.IProblemParser;
import org.e792a8.acme.core.workspace.IProblem;
import org.e792a8.acme.core.workspace.ITestPoint;
import org.e792a8.acme.utils.FileSystem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.gson.Gson;

public class VjudgeProblemParser extends WebParser implements IProblemParser {

	class ContestObj {
		String title;
		ProblemObj[] problems;

		class ProblemObj {
			String num;
			String title;
			String publicDescId;
			String publicDescVersion;
		}
	}

	class DescData {
		Section[] sections;

		class Section {
			String title;
			SecValue value;

			class SecValue {
				String content;
			}
		}
	}

	public VjudgeProblemParser(String url) {
		super(url);
	}

	@Override
	public IProblem parseTo(IProblem problem) {
		Document doc = readDocument();
		String url = getUrl();
		if (doc == null) {
			return null;
		}
		String urlRoot;
		try {
			urlRoot = url.replaceAll(new URL(url).getPath() + ".{0,}", "");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		String[] segs = url.split("/");
		String problemId = segs[segs.length - 1];
		Gson gson = new Gson();
		String dataJson = doc.getElementsByAttributeValue("name", "dataJson").text();
		ContestObj c = gson.fromJson(dataJson, ContestObj.class);
		String descSrc = urlRoot + "/problem/description/";
		for (ContestObj.ProblemObj o : c.problems) {
			if (o.num.equals(problemId)) {
				descSrc += o.publicDescId + "?" + o.publicDescVersion;
				Document descDoc = null;
				try {
					descDoc = Jsoup.connect(descSrc).timeout(20000).get();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (descDoc == null) {
					return null;
				}
				String descDataJson = descDoc.getElementsByClass("data-json-container").text();
				DescData dd = gson.fromJson(descDataJson, DescData.class);
				for (DescData.Section sec : dd.sections) {
					if ("Examples".equals(sec.title)) {
						Document examplesDoc = Jsoup.parse(sec.value.content);
						Iterator<Element> ie = examplesDoc.getElementsByTag("pre").iterator();
						while (ie.hasNext()) {
							String in = ie.next().html().replaceAll("<br>", "\n");
							if (!ie.hasNext()) {
								break;
							}
							String ans = ie.next().html().replaceAll("<br>", "\n");
							ITestPoint tp = problem.addTestPoint();
							FileSystem.write(tp.getInput().getFile(), in);
							FileSystem.write(tp.getAnswer().getFile(), ans);
						}
						break;
					}
				}
				break;
			}
		}
		return problem;
	}

}
