package com.tltt.lib.text.normalizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
	public final String REG_DELIMITER_TOKEN = "\\s|'";
	public Pattern patternDelimiter = Pattern.compile(REG_DELIMITER_TOKEN);

	private List<String> delimiters;
	private List<String> words;
	
	public Tokenizer(String text) {
		delimiters = explodeDelimiters(text);
		words =  explodeWords(text);
	}

	public List<String> explodeDelimiters(String text) {
		List<String> delimiters = new ArrayList<String>();
		Matcher matcher = patternDelimiter.matcher(text);
		while (matcher.find()) {
			delimiters.add(matcher.group());
		}
		return delimiters;
	}

	public List<String> explodeWords(String text) {
		String[] arr = text.split(REG_DELIMITER_TOKEN);
		return new ArrayList<String>(Arrays.asList(arr));
	}

	public void removeCurrentWord() {
		if (words.size() > 0) {
			words.remove(0);
			if (delimiters.size() > 0)
				delimiters.remove(0);
		}
	}

	public String joinTokens() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < words.size(); i++) {
			sb.append(words.get(i));
			if (i < delimiters.size())
				sb.append(delimiters.get(i));
		}
		return sb.toString();
	}

	public boolean hasNext() {
		return words.size() > 0;
	}
	
	public String nextWord() {
		if(hasNext()) {
			return words.get(0);
		}
		else
			return "";
	}
	
	public List<String> getDelimiters() {
		return delimiters;
	}

	public List<String> getData() {
		return words;
	}

}
