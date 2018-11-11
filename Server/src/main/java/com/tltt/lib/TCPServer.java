package com.tltt.lib;

import com.google.gson.Gson;
import com.tltt.lib.dto.QuidAnswerDTO;
import com.tltt.lib.dto.QuidQuestionDTO;

import text.NoPredictionException;

import java.io.DataInputStream;
import java.io.IOException;
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

    public TCPServer(String pHost, int pPort) {
        host = pHost;
        port = pPort;
        try {
            server = new ServerSocket(port, 100, InetAddress.getByName(host));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void open() {
        Thread t = new Thread(new Runnable() {
            public void run() {
            	logger.info("TCP SERVER LAUNCHED");
                while (isRunning == true) {
                    try {
                        client = server.accept();
                        logger.debug("Client connection opened");

                        try {
                            String content = readSocket();
                            logger.debug(content);
                            
                            QuidQuestionDTO questionContext =  gson.fromJson(content, QuidQuestionDTO.class);
                            QueryNavigator navigator = new QueryNavigator(questionContext);
                            navigator.openInBrowser();
                            QuidAnswerDTO mostRelevantAnswer = navigator.searchMostRelevantAnswer(questionContext);
                            logger.info(String.format("Most probable answer is : %s - %s", mostRelevantAnswer.getLabel(), mostRelevantAnswer.getTitle()));
                        } catch (NoPredictionException e) {
                        	logger.error("Cannot predict most relevant answer");
                        } catch (URISyntaxException e) {
                        	logger.error("Cannot open browser", e);
						}

                        client.close();
                        logger.debug("Client connection closed");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
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
}
