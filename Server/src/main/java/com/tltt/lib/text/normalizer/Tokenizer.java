package com.tltt.lib.text.normalizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
	public final String REG_DELIMITERS_TOKEN = "\\s|'";
	public Pattern patternDelimiter = Pattern.compile(REG_DELIMITERS_TOKEN);
	
	private List<String> delimiters;
	private List<String> words;
	private int index;
	
	public Tokenizer(String text) {
		delimiters = explodeDelimiters(text);
		words =  explodeWords(text);
		index = 0;
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
		String[] arr = text.split(REG_DELIMITERS_TOKEN);
		return new ArrayList<String>(Arrays.asList(arr));
	}

	public void removeCurrentWord() {
		if (words.size() > index) {
			words.remove(index);
			if (delimiters.size() > index)
				delimiters.remove(index);
		}
	}

	public String joinTokensWithDelimiters() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < words.size(); i++) { 
			sb.append(words.get(i));
			if (i < delimiters.size())
				sb.append(delimiters.get(i));
		}
		return sb.toString();
	}
	
	public String joinTokensWithSpaces() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < words.size(); i++) {
			sb.append(words.get(i));
			if (i < words.size() - 1) {
				sb.append(' ');
			}
		}
		return sb.toString();
	}
	
	public boolean hasWord() {
		return words.size() > index;
	}
	
	public String currentWord() {
		if(hasWord()) {
			return words.get(index);
		}
		return "";
	}
	
	public void skipCurrentWord() {
		index++;
	}
	
	public int wordsCount() {
		return words.size();
	}
	
	public List<String> getDelimiters() {
		return delimiters;
	}

	public List<String> getData() {
		return words;
	}

}
