package com.tltt.lib.text;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tltt.lib.dto.QuidAnswerDTO;
import com.tltt.lib.dto.QuidQuestionDTO;
import com.tltt.lib.text.normalizer.QueryNormalizer;

public class OccurencesSearcher {

	private static Logger logger = LogManager.getLogger();

	private QueryNormalizer queryNormalizer;
	private QuidQuestionDTO quidQuestionDTO;
	private String textToSearch;

	public OccurencesSearcher(QuidQuestionDTO quidQuestionDTO, String textToSearch) {
		queryNormalizer = new QueryNormalizer();
		this.quidQuestionDTO = quidQuestionDTO;
		this.textToSearch = textToSearch;
		normalizeElements();
	}

	private void normalizeElements() {
		textToSearch = QueryNormalizer.removeDiacritics(textToSearch);
		String questionEntitledNorm = QueryNormalizer.removeDoubleQuotes(quidQuestionDTO.getQuestionEntitled());
		quidQuestionDTO.setQuestionEntitled(questionEntitledNorm);

		for (QuidAnswerDTO quidAnswerDTO : quidQuestionDTO.getAnswers()) {
			String normAnswerTitle = quidAnswerDTO.getTitle();
			normAnswerTitle = QueryNormalizer.removeDiacritics(normAnswerTitle);
			normAnswerTitle = queryNormalizer.removeLeadingStopWords(normAnswerTitle);
			quidAnswerDTO.setTitle(normAnswerTitle);
		}
	}

	public QuidAnswerDTO getMostOccuredAnswer() throws NoPredictionException {
		Map<QuidAnswerDTO, Integer> answersOccurences = new LinkedHashMap<>();
		for (QuidAnswerDTO quidAnswerDTO : quidQuestionDTO.getAnswers()) {
			int occurences = countWord(quidAnswerDTO.getTitle());
			answersOccurences.put(quidAnswerDTO, occurences);
		}
		logOccurencesMap(answersOccurences);
		QuidAnswerDTO mostAccurateAnswer = getMostFrequentAnswer(answersOccurences);
		return mostAccurateAnswer;
	}

	public Map<String, Integer> extractNumbers() {
		Map<String, Integer> numbersOccurences = new LinkedHashMap<>();

		Pattern p = Pattern.compile("\\b[0-9]*\\b");
		Matcher m = p.matcher(textToSearch);
		while (m.find()) {
			String occurence = m.group();
			int count = numbersOccurences.containsKey(occurence) ? numbersOccurences.get(occurence) : 0;
			numbersOccurences.put(occurence, count + 1);
		}

		return numbersOccurences;
	}

	public static QuidAnswerDTO getMostFrequentAnswer(Map<QuidAnswerDTO, Integer> answersOccurences) throws NoPredictionException {
		Entry<QuidAnswerDTO, Integer> mostRelevantEntry = answersOccurences.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get();

		for (Entry<QuidAnswerDTO, Integer> entry : answersOccurences.entrySet()) {
			if (!entry.equals(mostRelevantEntry) && entry.getValue().intValue() == mostRelevantEntry.getValue().intValue())
				throw new NoPredictionException();
		}

		return mostRelevantEntry.getKey();
	}

	public int countWord(String searchedWord) {
		int count = 0;
		String regSearchedWord = String.format("\\b%s\\b", Pattern.quote(searchedWord));
		Pattern p = Pattern.compile(regSearchedWord, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(textToSearch);
		while (m.find())
			count++;
		return count;
	}

	public static void logOccurencesMap(Map<QuidAnswerDTO, Integer> answersOccurences) {
		logger.debug("--------Occurences---------");
		for (Map.Entry<QuidAnswerDTO, Integer> entry : answersOccurences.entrySet()) {
			logger.debug(String.format("%1s - %-25s %d times found", entry.getKey().getLabel(), entry.getKey().getTitle(), entry.getValue()));
		}
		logger.debug("---------------------------");
	}
	
	public static void logOccurencesIntMap(Map<String, Integer> numbersOccurences) {
		logger.debug("--------Occurences---------");
		for (Entry<String, Integer> entry : numbersOccurences.entrySet()) {
			logger.debug(String.format("%s \t\t\t%d times found", entry.getKey(), entry.getValue()));
		}
		logger.debug("---------------------------");
	}
}
