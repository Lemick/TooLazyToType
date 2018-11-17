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

import com.tltt.lib.text.OccurencesSearcher;

public class Main { 
	
	private static Logger logger = LogManager.getLogger();
	
	public static TCPServer server;

	public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {
		//testDlJsoup("https://www.google.com/search?q=En+quelle+ann%C3%A9e+eut+lieu+le+concert+du+Live-Aid%2C+consid%C3%A9r%C3%A9+comme+l%27un+des+plus+grands+concerts+de+l%27histoire+%3F");
		//OccurencesSearcher occurencesSearcher = new OccurencesSearcher(quidQuestionDTO, textToSearch)
		
		// QuidQuestionDTO questionContext = new QuidQuestionDTO();
		// questionContext.setQuestionEntitled("");
		// Navigate navigate = new Navigate(questionContext);
		// navigate.openSearchPage();
		
		launchServer();

	}

	public static void launchServer() throws InterruptedException {
		server = new TCPServer("0.0.0.0", 5000);
		server.open();

		while (server.isRunning())
			TimeUnit.SECONDS.sleep(1);

	}

	private static void testDlJsoup(String url) throws IOException {
		String html = (new HTMLBuilder(url)).cleanMetaCode(true).removeAccents(true).build();
		FileUtils.writeStringToFile(new File("test.txt"), html);
	}
	

	
}
