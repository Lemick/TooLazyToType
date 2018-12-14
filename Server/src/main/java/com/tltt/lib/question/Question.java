package com.tltt.lib.question;

import java.util.ArrayList;
import java.util.List;

public class Question {
	private String id;
	private String title;
	private List<Answer> answers;

	public Question() {
		answers = new ArrayList<Answer>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	@Override
	public String toString() {
		return id + "-" + title;
	}
}
