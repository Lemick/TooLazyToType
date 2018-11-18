package com.tltt.lib;

import java.util.Map;

import com.tltt.lib.dto.QuidAnswerDTO;
import com.tltt.lib.dto.QuidQuestionDTO;
import com.tltt.lib.text.StringBuilderLiner;

public class ReportBuilder {

	private final static String HEADER = "------------ %s ------------";

	private String url;
	private Map<QuidAnswerDTO, Integer> answersOccurences;
	private QuidQuestionDTO question;

	public ReportBuilder() {
		url = null;
		answersOccurences = null;
		question = null;
	}

	public ReportBuilder queryUrl(String url) {
		this.url = url;
		return this;
	}

	public ReportBuilder answersOccurences(Map<QuidAnswerDTO, Integer> answersOccurences) {
		this.answersOccurences = answersOccurences;
		return this;
	}

	public ReportBuilder question(QuidQuestionDTO question) {
		this.question = question;
		return this;
	}

	public String build() {
		StringBuilderLiner sbl = new StringBuilderLiner();
		
		if (question != null) {
			sbl.appendLine(String.format(HEADER, "Question"));
			sbl.appendLine(question.getQuestionEntitled());
		}
		if (url != null) {
			sbl.appendLine(String.format(HEADER, "URL Google"));
			sbl.appendLine(url);
		}
		if (answersOccurences != null) {
			sbl.appendLine(String.format(HEADER, "Occurences trouvées dans la page Google"));
			for (Map.Entry<QuidAnswerDTO, Integer> entry : answersOccurences.entrySet()) {
				sbl.appendLine(String.format("%1s - %-30s %d times found", entry.getKey().getLabel(), entry.getKey().getTitle(), entry.getValue()));
			}
		}
		
		return sbl.toString();
	}

}
