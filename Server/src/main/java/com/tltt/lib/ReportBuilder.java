package com.tltt.lib;

import java.util.Map;
import java.util.Map.Entry;

import com.tltt.lib.dto.QuidAnswerDTO;
import com.tltt.lib.dto.QuidQuestionDTO;
import com.tltt.lib.question.Answer;
import com.tltt.lib.question.Question;
import com.tltt.lib.text.StringBuilderLiner;

public class ReportBuilder {

	private final static String HEADER = "------------ %s ------------";

	private String url;
	private Map<Answer, Integer> answersOccurences;
	private Question question;

	public ReportBuilder() {
		url = null;
		answersOccurences = null;
		question = null;
	}

	public ReportBuilder queryUrl(String url) {
		this.url = url;
		return this;
	}

	public ReportBuilder answersOccurences(Map<Answer, Integer> answersOccurences) {
		this.answersOccurences = answersOccurences;
		return this;
	}

	public ReportBuilder question(Question question) {
		this.question = question;
		return this;
	}

	public String build() {
		StringBuilderLiner sbl = new StringBuilderLiner();
		
		if (question != null) {
			sbl.appendLine(String.format(HEADER, "Question"));
			sbl.appendLine(question.getTitle());
		}
		if (url != null) {
			sbl.appendLine(String.format(HEADER, "URL Google"));
			sbl.appendLine(url);
		}
		if (answersOccurences != null) {
			sbl.appendLine(String.format(HEADER, "Occurences found with Google"));
			for (Entry<Answer, Integer> entry : answersOccurences.entrySet()) {
				sbl.appendLine(String.format("%1s - %-30s %d times found", entry.getKey().getId(), entry.getKey().getTitle(), entry.getValue()));
			}
		}
		sbl.appendLine("");
		return sbl.toString();
	}

}
