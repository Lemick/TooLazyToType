package com.tltt.lib.text;

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

	private QuidQuestionDTO quidQuestionDTO;
	private String textToSearch;
	private Map<QuidAnswerDTO, Integer> answersOccurences;

	public OccurencesSearcher(QuidQuestionDTO quidQuestionDTO, String textToSearch) {;
		this.quidQuestionDTO = quidQuestionDTO;
		this.textToSearch = textToSearch;
		answersOccurences = new LinkedHashMap<>();
		normalizeElements();
		buildOccurencesMap();
	}

	private void normalizeElements() {
		textToSearch = QueryNormalizer.getInstance().removeDiacritics(textToSearch);
		String questionEntitledNorm = QueryNormalizer.getInstance().removeDoubleQuotes(quidQuestionDTO.getQuestionEntitled());
		quidQuestionDTO.setQuestionEntitled(questionEntitledNorm);

		for (QuidAnswerDTO quidAnswerDTO : quidQuestionDTO.getAnswers()) {
			String normAnswerTitle = quidAnswerDTO.getTitle();
			normAnswerTitle = QueryNormalizer.getInstance().removeDiacritics(normAnswerTitle);
			normAnswerTitle = QueryNormalizer.getInstance().removeLeadingStopWords(normAnswerTitle);
			quidAnswerDTO.setTitle(normAnswerTitle);
		}
	}

	public QuidAnswerDTO predictAnswer() throws NoPredictionException {
		QuidAnswerDTO mostAccurateAnswer = getMostFrequentAnswer();
		return mostAccurateAnswer;
	}

	private void buildOccurencesMap() {
		for (QuidAnswerDTO quidAnswerDTO : quidQuestionDTO.getAnswers()) {
			int occurences = countWord(quidAnswerDTO.getTitle());
			answersOccurences.put(quidAnswerDTO, occurences);
		}
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

	public QuidAnswerDTO getMostFrequentAnswer() throws NoPredictionException {
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

	public Map<QuidAnswerDTO, Integer> getAnswersOccurences() {
		return answersOccurences;
	}
}
