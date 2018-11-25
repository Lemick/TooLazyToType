package com.tltt.lib.html;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import com.tltt.lib.text.normalizer.QueryNormalizer;

public class HTMLExtractor {

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0";
	private static Logger logger = LogManager.getLogger(HTMLExtractor.class);

	private HTMLBuildConfiguration configuration;
	private SublinksExtractor sublinkExtractor;

	public HTMLExtractor(HTMLBuildConfiguration configuration) {
		this.configuration = configuration;
	}

	public String build() {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			logger.debug(String.format("Get URL : %s", configuration.getUrlQuery()));
			Document document = Jsoup.connect(configuration.getUrlQuery()).userAgent(USER_AGENT).get();
			sublinkExtractor = new GoogleSublinkExtractor(document);

			if (configuration.hasToCleanMetaCode()) {
				cleanMetaCode(document);
			}
			String rootHtml = document.html();
			if (configuration.hasToRemoveAccent()) {
				rootHtml = QueryNormalizer.getInstance().removeDiacritics(rootHtml);
			}
			stringBuilder.append(rootHtml);
			appendSublinksHtml(stringBuilder);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringBuilder.toString();
	}

	private void cleanMetaCode(Document doc) {
		doc.select("script,style").remove();
		removeAttributes(doc);
	}

	private void removeAttributes(Document doc) {
		final List<String> whitelistedAttributes = Arrays.asList(new String[] { "href" });
		final List<String> whitelistedClasses = Arrays.asList(new String[] { "r" }); // TODO
																						// abstract
																						// Configclass

		Elements elements = doc.getAllElements();
		for (Element elem : elements) {
			List<String> attToRemove = new ArrayList<>();
			Attributes attributes = elem.attributes();
			for (Attribute attribute : attributes) {
				if (!whitelistedAttributes.contains(attribute.getKey()) && attribute.getKey().equals("class") && !whitelistedClasses.contains(attribute.getValue()))
					attToRemove.add(attribute.getKey());
			}

			for (String att : attToRemove) {
				elem.removeAttr(att);
			}
		}
	}

	private void appendSublinksHtml(StringBuilder stringBuilder) {

		for (int i = 0; i < configuration.getNbSublinkstoExtract(); i++) {
			String nextSublink = sublinkExtractor.getNextSublink();
			if (nextSublink != null) {
				HTMLBuildConfiguration sublinksConfig = new HTMLBuildConfiguration(this.configuration).subLinksToExplore(0).urlQuery(nextSublink);
				HTMLExtractor htmlExtractor = new HTMLExtractor(sublinksConfig);
				stringBuilder.append(String.format("<!-- Following HTML is extracted from %s -->", sublinksConfig.getUrlQuery()));
				stringBuilder.append(htmlExtractor.build());
			}
		}
	}
}
