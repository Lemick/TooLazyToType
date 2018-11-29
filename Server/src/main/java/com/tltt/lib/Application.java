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
import com.tltt.lib.file.ResourceFile;
import com.tltt.lib.html.HTMLBuildConfiguration;
import com.tltt.lib.html.HTMLExtractor;
import com.tltt.lib.question.Question;
import com.tltt.lib.text.OccurencesSearcher;

public class Application {

	private static int TCP_PORT = 5000;
	private static Logger logger = LogManager.getLogger();

	public static TCPServer server;
	private static DiscordWebhook discordWebhook;
	
	public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {
		initializeConfiguration();
		launchServer();
	}
	
	public static void launchServer() throws InterruptedException {
		server = new TCPServer("0.0.0.0", TCP_PORT);
		server.setDiscordWebhook(discordWebhook);
		server.open();

		while (server.isRunning())
			TimeUnit.SECONDS.sleep(1);
	}

	private static void initializeConfiguration() {
		initDiscordWebhook();
	}

	public static void initDiscordWebhook() {
		final String RESOURCEPATH_DISC_URL_WEBHOOK = "discordwebhook.txt";
		try {
			ResourceFile resource = new ResourceFile(RESOURCEPATH_DISC_URL_WEBHOOK);
			logger.info("Discord webhook configured");
			String urlWebhook = resource.getLines().get(0);
			discordWebhook = new DiscordWebhook(urlWebhook);
		} catch (Exception e) {
			logger.error(String.format("Error during reading file %s for retrieving discord URL, cannot webhook to Discord", RESOURCEPATH_DISC_URL_WEBHOOK), e);
		}
	}

}
