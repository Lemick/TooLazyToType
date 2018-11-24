package com.tltt.lib;

import com.google.gson.Gson;
import com.tltt.lib.dto.QuidAnswerDTO;
import com.tltt.lib.dto.QuidQuestionDTO;
import com.tltt.lib.file.ResourceFile;
import com.tltt.lib.question.Answer;
import com.tltt.lib.question.Question;
import com.tltt.lib.question.QuestionConverter;
import com.tltt.lib.text.NoPredictionException;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TCPServer {

	private static Logger logger = LogManager.getLogger();

	private int port;
	private String host;
	private ServerSocket server = null;
	private Socket client;
	private boolean isRunning = true;
	private Gson gson = new Gson();

	private DiscordWebhook discordWebhook;

	public TCPServer(String pHost, int pPort) {
		host = pHost;
		port = pPort;
		try {
			server = new ServerSocket(port, 100, InetAddress.getByName(host));
			initDiscordWebhook();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void open() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				logger.info("TCP Server launched, waiting for questions");
				while (isRunning == true) {
					try {
						client = server.accept();
						logger.debug("Client connection opened");
						String content = readSocket();
						logger.debug(content);
						QuidQuestionDTO questionContextDTO = gson.fromJson(content, QuidQuestionDTO.class);
						Question questionContext = QuestionConverter.convertToQuestion(questionContextDTO);
						doSearch(questionContext);
						client.close();
						logger.debug("Client connection closed");
					} catch (IOException e) {
						e.printStackTrace();
					} catch (NoPredictionException e) {
						logger.error("Cannot predict most relevant answer");
					} catch (URISyntaxException e) {
						logger.error("Cannot open browser", e);
					}
				}
			}

		});
		t.start();

	}

	private void doSearch(Question questionContext) throws UnsupportedEncodingException, URISyntaxException, IOException, NoPredictionException {
		QueryNavigator navigator = new QueryNavigator(questionContext);
		navigator.openInBrowser();
		navigator.publishReport(discordWebhook);
		try {
			Answer mostRelevantAnswer = navigator.searchMostRelevantAnswer(questionContext);
			logger.info(String.format("Most probable answer is : %s - %s", mostRelevantAnswer.getTitle(), mostRelevantAnswer.getTitle()));
		} catch (NoPredictionException e) {
			logger.error("No best answer found");
		}
	}

	public void close() {
		isRunning = false;
	}

	private String readSocket() {
		final int BUFFER_SIZE = 1000;
		byte[] messageByte = new byte[BUFFER_SIZE];
		boolean end = false;
		StringBuilder sb = new StringBuilder();

		try {
			DataInputStream in = new DataInputStream(client.getInputStream());
			while (!end) {
				int bytesRead = in.read(messageByte);
				sb.append(new String(messageByte, 0, bytesRead));
				if (bytesRead < BUFFER_SIZE) {
					end = true;
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void initDiscordWebhook() {
		final String RESOURCEPATH_DISC_URL_WEBHOOK = "discordwebhook.txt";
		try {
			ResourceFile resource = new ResourceFile(RESOURCEPATH_DISC_URL_WEBHOOK);
			logger.info("Discord webhook configured");
			String urlWebhook = resource.getLines().get(0);
			this.discordWebhook = new DiscordWebhook(urlWebhook);
		} catch (Exception e) {
			logger.error(String.format("Error during reading file %s for retrieving discord URL, cannot webhook to Discord", RESOURCEPATH_DISC_URL_WEBHOOK), e);
		}
	}
}
