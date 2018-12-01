package com.tltt.lib;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tltt.lib.html.HTMLBuildConfiguration;
import com.tltt.lib.html.HTMLExtractor;
import com.tltt.lib.question.Question;
import com.tltt.lib.text.OccurencesSearcher;

/**
 * NOTE TODO 
 * Bug quand les réponses sont des chiffres (le JS est aussi parsé et fausse les resultats) 
 * Faire une implem différente pour la derniére question chercher tout les chiffres de la page Parser le contenu de la premiere
 * page pour avoir de meilleurs résultats ? oui Parser si la question est une
 * question négative ?
 * Implementer la normalization des intutulés de question
 * Normalisation des élements de réponses enlevés les plueriels( question sulky)
 */
public class QueryNavigator {

	private static Logger logger = LogManager.getLogger();
	private String urlQuery;
	private OccurencesSearcher occurencesSearcher;
	private Question question;

	public QueryNavigator(Question question) throws UnsupportedEncodingException {
		this.question = question;
		this.urlQuery = new URLGenerator(question).getUrlQuery();
		
		SuperStopWatch stopWatch = new SuperStopWatch();
		HTMLBuildConfiguration config = new HTMLBuildConfiguration(urlQuery).removeAccents(true).cleanMetaCode(true).subLinksToExplore(1);
		String html = new HTMLExtractor(config).build();
		logger.debug(String.format("HTML parsed took %s ms", stopWatch.getMsElaspedAndRestart()));
		
		occurencesSearcher = new OccurencesSearcher(question, html);
		logger.debug(String.format("Occurences extracted took %s ms",  stopWatch.getMsElaspedAndRestart()));
	}

	public void openInBrowser() throws URISyntaxException, IOException {
		URI uri = new URI(urlQuery);
		java.awt.Desktop.getDesktop().browse(uri);
	}

	public void publishReport(DiscordWebhook discordWebhook) {
		ReportBuilder reportBuilder = new ReportBuilder().question(question).queryUrl(urlQuery).answersOccurences(occurencesSearcher.getAnswersOccurences());
		String strReport = reportBuilder.build();
		if (discordWebhook != null) {
			discordWebhook.addMessage(strReport);
			discordWebhook.send();
		}
		logger.debug(strReport);
	}

}
