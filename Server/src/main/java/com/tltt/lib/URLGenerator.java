package com.tltt.lib;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.tltt.lib.dto.QuidQuestionDTO;
import com.tltt.lib.text.normalizer.QueryNormalizer;

public class URLGenerator {

	public static final String BASE_SEARCH_URL = "https://www.google.com/search?q=%s";
	public static final String BASE_TOPICAL_SEARCH_URL = "https://www.google.com/search?q=%s&tbm=nws";

	private String urlQuery;
	private QueryNormalizer normalizer;

	public URLGenerator(QuidQuestionDTO quidQuestionDTO) {
		normalizer = new QueryNormalizer();
		buildURL(quidQuestionDTO);
	}

	private void buildURL(QuidQuestionDTO quidQuestionDTO) {
		try {
			if (normalizer.isTopicalSubjectSentence(quidQuestionDTO.getQuestionEntitled())) {
				urlQuery = String.format(BASE_TOPICAL_SEARCH_URL, URLEncoder.encode(quidQuestionDTO.getQuestionEntitled(), "UTF-8"));
			} else {
				urlQuery = String.format(BASE_SEARCH_URL, URLEncoder.encode(quidQuestionDTO.getQuestionEntitled(), "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String getUrlQuery() {
		return urlQuery;
	}

}
