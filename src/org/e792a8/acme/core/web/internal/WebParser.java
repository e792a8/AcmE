package org.e792a8.acme.core.web.internal;

import java.io.IOException;

import org.jsoup.Jsoup;

public abstract class WebParser {

	private String url;

	public String getUrl() {
		return url;
	}

	public WebParser(String url) {
		this.url = url;
	}

	public org.jsoup.nodes.Document readDocument() {
		try {
			return Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
