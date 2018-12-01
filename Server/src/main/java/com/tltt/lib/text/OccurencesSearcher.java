package com.tltt.lib.text;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tltt.lib.question.Answer;
import com.tltt.lib.question.Question;
import com.tltt.lib.text.normalizer.QueryNormalizer;

public class OccurencesSearcher {

	private static Logger logger = LogManager.getLogger();

	private Question question;
	private String textToSearch;
	private Map<Answer, Integer> answersOccurences;

	public OccurencesSearcher(Question question, String textToSearch) {;
		this.question = question;
		this.textToSearch = textToSearch;
		answersOccurences = new LinkedHashMap<>();
		normalizeElements();
		buildOccurencesMap();
	}

	private void normalizeElements() {
		textToSearch = QueryNormalizer.getInstance().removeDiacritics(textToSearch);
		String questionEntitledNorm = QueryNormalizer.getInstance().removeDoubleQuotes(question.getTitle());
		question.setTitle(questionEntitledNorm);

		for (Answer answer : question.getAnswers()) {
			String normAnswerTitle = answer.getTitle();
			normAnswerTitle = QueryNormalizer.getInstance().removeLeadingStopWords(normAnswerTitle);
			normAnswerTitle = QueryNormalizer.getInstance().removeDiacritics(normAnswerTitle);
			answer.setTitle(normAnswerTitle);
		}
	}

	public Answer predictAnswer() throws NoPredictionException {
		Answer mostAccurateAnswer = getMostFrequentAnswer();
		return mostAccurateAnswer;
	}

	private void buildOccurencesMap() {
		for (Answer answer : question.getAnswers()) {
			int occurences = countWord(answer.getTitle());
			answersOccurences.put(answer, occurences);
		}
	}

	public Map<String, Integer> extractNumbers() throws IOException {
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

	public Answer getMostFrequentAnswer() throws NoPredictionException {
		Entry<Answer, Integer> mostRelevantEntry = answersOccurences.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get();

		for (Entry<Answer, Integer> entry : answersOccurences.entrySet()) {
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

	public Map<Answer, Integer> getAnswersOccurences() {
		return answersOccurences;
	}
}
