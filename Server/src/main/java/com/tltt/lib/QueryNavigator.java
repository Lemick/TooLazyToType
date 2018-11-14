package com.tltt.lib;

import com.tltt.lib.dto.QuidAnswerDTO;
import com.tltt.lib.dto.QuidQuestionDTO;

import text.NoPredictionException;
import text.OccurencesSearcher;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * NOTE TODO Bug quand les réponses sont des chiffres (le JS est aussi parsé et
 * fausse les resultats) Faire une implem différente pour la derniére question
 * -> chercher tout les chiffres de la page Enlever les escapes de """ dans les
 * questions
 * Quand detect "cette semaine" mettre dans actualité
 */
public class QueryNavigator {

	public static final String BASE_URL = "https://www.google.com/search?q=%s";

	private String urlQuery;
	private QuidQuestionDTO quidQuestionDTO;

	public QueryNavigator(QuidQuestionDTO quidQuestionDTO) throws UnsupportedEncodingException {
		this.quidQuestionDTO = quidQuestionDTO;
		buildUrl();
	}

	public QuidAnswerDTO searchMostRelevantAnswer(QuidQuestionDTO quidQuestionDTO) throws NoPredictionException {
		String html;
		QuidAnswerDTO quidAnswerDTO = null;
		html = new HTMLBuilder(urlQuery).removeAccents(true).build();
		OccurencesSearcher occurencesSearcher = new OccurencesSearcher(quidQuestionDTO, html);
		quidAnswerDTO = occurencesSearcher.getMostOccuredAnswer();
		return quidAnswerDTO;
	}

	public void openInBrowser() throws URISyntaxException, IOException {
		URI uri = new URI(urlQuery);
		java.awt.Desktop.getDesktop().browse(uri);
	}

	private void buildUrl() throws UnsupportedEncodingException {
		urlQuery = String.format(BASE_URL, URLEncoder.encode(quidQuestionDTO.getQuestionEntitled(), "UTF-8"));
	}

}
