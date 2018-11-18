package com.tltt.lib;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class DiscordWebhook {

	private static final String BODY_CONTENT = "content";

	private String url;
	private StringBuilder buffer;
	
	public DiscordWebhook(String url) {
		this.url = url;
		buffer = new StringBuilder();
	}


	public void send() {
		sendMessage(buffer.toString());
		buffer = new StringBuilder();
	}
	
	public void addMessageLine(String message) {
		buffer.append(message).append("\n");
	}
	
	public void addMessage(String message) {
		buffer.append(message);
	}
	
	private void sendMessage(String content) {
		try {
			Unirest.post(url).field(BODY_CONTENT, content).asString();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}
}
