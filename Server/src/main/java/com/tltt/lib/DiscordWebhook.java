package com.tltt.lib;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tltt.lib.text.StringBuilderLiner;

public class DiscordWebhook {

	private static final String BODY_CONTENT = "content";

	private String url;
	private StringBuilderLiner buffer;
	
	public DiscordWebhook(String url) {
		this.url = url;
		buffer = new StringBuilderLiner();
	}


	public void send() {
		sendMessage(buffer.toString());
		buffer = new StringBuilderLiner();
	}
	
	public void addMessageLine(String message) {
		buffer.appendLine(message);
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
