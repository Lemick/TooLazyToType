package com.tltt.lib.html;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.Scanner;

public abstract class SublinksExtractor {
	
	protected Document document;
	protected int indexChild;

	public SublinksExtractor(Document document) {
		this.document = document;
		this.indexChild = 0;
	}

	public String getNextSublink() {
		Element nextElement = getNextLinkElement();
		if (nextElement == null)
			return null;
		else {
			indexChild++;
			return nextElement.attr(getAttributeKeyToExtract());
		}
	}

	protected abstract Element getNextLinkElement();

	protected abstract String getAttributeKeyToExtract();
}
