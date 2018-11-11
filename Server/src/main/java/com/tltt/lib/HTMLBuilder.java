package com.tltt.lib;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import text.normalizer.QueryNormalizer;

public class HTMLBuilder {

	private static Logger logger = LogManager.getLogger(HTMLBuilder.class);
	
	private String urlQuery;
	private boolean hasToRemoveAccent;
	private boolean hasToCleanMetaCode;

	public HTMLBuilder(String urlQuery) {
		this.urlQuery = urlQuery;
		hasToRemoveAccent = false;
		hasToCleanMetaCode = false;
	}

	public HTMLBuilder setSearchQueryHtml(String urlQuery) throws IOException {
		this.urlQuery = urlQuery;
		return this;
	}

	public HTMLBuilder removeAccents(boolean hasToRemoveAccent) {
		this.hasToRemoveAccent = hasToRemoveAccent;
		return this;
	}

	public HTMLBuilder cleanMetaCode(boolean hasToCleanMetaCode) {
		this.hasToCleanMetaCode = hasToCleanMetaCode;
		return this;
	}

	
	public String build() {
		String resultHtml = "";
		try {
			logger.debug(String.format("Get URL : %s", urlQuery));
			Document doc = Jsoup.connect(urlQuery).get();
			
			if (hasToCleanMetaCode) {
				doc.select("script,style").remove();
			}
			
			resultHtml = doc.html();
			if (hasToRemoveAccent) {
				resultHtml = QueryNormalizer.removeDiacritics(resultHtml);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultHtml;
	}
	

}
