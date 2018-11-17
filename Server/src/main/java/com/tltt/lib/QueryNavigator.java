package com.tltt.lib;

import com.tltt.lib.dto.QuidAnswerDTO;
import com.tltt.lib.dto.QuidQuestionDTO;
import com.tltt.lib.text.NoPredictionException;
import com.tltt.lib.text.OccurencesSearcher;

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
 * NOTE TODO Bug quand les réponses sont des chiffres (le JS est aussi parsé etfausse les resultats) Faire une implem différente pour la derniére question
 * -> chercher tout les chiffres de la page
 * Parser le contenu de la premiere page pour avoir de meilleurs résultats ?
 */
public class QueryNavigator {

	
	private String urlQuery;

	public QueryNavigator(QuidQuestionDTO quidQuestionDTO) throws UnsupportedEncodingException {
		URLGenerator urlGenerator = new URLGenerator(quidQuestionDTO);
		urlQuery = urlGenerator.getUrlQuery();
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

}
