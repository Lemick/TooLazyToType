package com.tltt.lib;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.tltt.lib.dto.QuidQuestionDTO;
import com.tltt.lib.question.Question;
import com.tltt.lib.text.normalizer.QueryNormalizer;

public class URLGenerator {

	public static final String BASE_SEARCH_URL = "https://www.google.com/search?q=%s";
	public static final String BASE_TOPICAL_SEARCH_URL = "https://www.google.com/search?q=%s&tbm=nws";

	private String urlQuery;
	private QueryNormalizer normalizer;

	public URLGenerator(Question question) {
		normalizer = QueryNormalizer.getInstance();
		buildURL(question);
	}

	private void buildURL(Question question) {
		try {
			if (normalizer.isTopicalSubjectSentence(question.getTitle())) {
				urlQuery = String.format(BASE_TOPICAL_SEARCH_URL, URLEncoder.encode(question.getTitle(), "UTF-8"));
			} else {
				urlQuery = String.format(BASE_SEARCH_URL, URLEncoder.encode(question.getTitle(), "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String getUrlQuery() {
		return urlQuery;
	}

}
