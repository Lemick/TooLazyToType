package com.tltt.lib.text.normalizer;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.tltt.lib.dto.QuidAnswerDTO;
import com.tltt.lib.dto.QuidQuestionDTO;
import com.tltt.lib.file.ResourceFile;

public final class QueryNormalizer {

	private static final String STOP_WORDS_FILENAME = "normalizer/stop_words_fr.txt";
	private static final String TOPICAL_SUBJECTS_FILENAME = "normalizer/topical_subjects_words_fr.txt";
	
	private static final QueryNormalizer instance = new QueryNormalizer();
	
	private List<String> stopWords;
	private List<String> topicalSubjectsWords;
	
	public static QueryNormalizer getInstance() {
		return instance;
	}
	
	private QueryNormalizer() {
		loadStopWordsFile();
		loadTopicalSubjectsFile();
	}

	private void loadTopicalSubjectsFile() {
		ResourceFile file = new ResourceFile(STOP_WORDS_FILENAME);
		stopWords = file.getLines();
	}

	private void loadStopWordsFile() {
		ResourceFile file = new ResourceFile(TOPICAL_SUBJECTS_FILENAME);
		topicalSubjectsWords = file.getLines();
	}

	public String removeDiacritics(String text) {
		return Normalizer.normalize(text, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

	public String removeDoubleQuotes(String text) {
		return text.replace("\"", "");
	}

	public String removeLeadingStopWords(String text) {	
		String lowerCased = text.toLowerCase();
		Tokenizer tokenizer = new Tokenizer(lowerCased);	
		
		while (tokenizer.hasWord()) {
			String token = tokenizer.currentWord();
			if (isStopWord(token))
				tokenizer.removeCurrentWord();
			else
				break;
		}
		String result = tokenizer.joinTokensWithDelimiters();
		return result;
	}
	
	public String removeAllStopWords(String text) {	
		String lowerCased = text.toLowerCase();
		Tokenizer tokenizer = new Tokenizer(lowerCased);	
		
		while (tokenizer.hasWord()) {
			String token = tokenizer.currentWord();
			if (isStopWord(token))
				tokenizer.removeCurrentWord();
			else
				tokenizer.skipCurrentWord();
		}
		String result = tokenizer.joinTokensWithDelimiters();
		return result;
	}

	public boolean isStopWord(String word) {
		word = word.toLowerCase();
		for (String stopWord : stopWords) {
			if (word.equals(stopWord))
				return true;
		}
		return false;
	}
	
	public boolean isTopicalSubjectSentence(String sentence) {
		sentence = sentence.toLowerCase();
		for (String topicalSubjectsWord : topicalSubjectsWords) {
			if (sentence.contains(topicalSubjectsWord)) 
				return true;
		}
		return false;
	}
	

}
