package com.tltt.lib.html;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleSublinkExtractor extends SublinksExtractor {

	private Elements sublinks = null;

	public GoogleSublinkExtractor(Document document) {
		super(document);
		initLinks();
	}

	private void initLinks() {
		sublinks = document.select(".r > a:first-of-type");
	}

	@Override
	protected Element getNextLinkElement() {
		if(sublinks.size() > indexChild)
			return sublinks.get(indexChild);
		else 
			return null;
	}

	@Override
	protected String getAttributeKeyToExtract() {
		final String keyToExtract = "href";
		return keyToExtract;
	}

}
