package com.tltt.lib;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;

import com.tltt.lib.dto.QuidAnswerDTO;
import com.tltt.lib.dto.QuidQuestionDTO;
import com.tltt.lib.html.HTMLBuildConfiguration;
import com.tltt.lib.html.HTMLExtractor;
import com.tltt.lib.question.Question;
import com.tltt.lib.text.OccurencesSearcher;

public class Main {

	private static int TCP_PORT = 5000;
	private static Logger logger = LogManager.getLogger();

	public static TCPServer server;

	public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {
		launchServer();
	}

	public static void launchServer() throws InterruptedException {
		server = new TCPServer("0.0.0.0", TCP_PORT);
		server.open();

		while (server.isRunning())
			TimeUnit.SECONDS.sleep(1);
	}


	/**
	 * TEST UTILS
	 */
	private static void testDlJsoup(String url) throws IOException {
		HTMLBuildConfiguration config = new HTMLBuildConfiguration(url).cleanMetaCode(true).removeAccents(true).subLinksToExplore(2);
		String html = new HTMLExtractor(config).build();
		FileUtils.writeStringToFile(new File("test.txt"), html);
	}
	
	private static void testOccurences(Question question) throws IOException {
		QueryNavigator navigator = new QueryNavigator(question);
		navigator.publishReport(null);
	}
}
