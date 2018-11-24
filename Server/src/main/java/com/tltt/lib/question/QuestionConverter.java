package com.tltt.lib.question;

import java.util.ArrayList;
import java.util.List;

import com.tltt.lib.dto.QuidAnswerDTO;
import com.tltt.lib.dto.QuidQuestionDTO;

public class QuestionConverter {

	public static Question convertToQuestion(QuidQuestionDTO quidQuestionDTO) {
		Question questionResult = new Question();
		questionResult.setId("");
		questionResult.setTitle(quidQuestionDTO.getQuestionEntitled());
		questionResult.setAnswers(convertToAnswers(quidQuestionDTO.getAnswers()));
		return questionResult;
	}
	
	public static List<Answer> convertToAnswers(List<QuidAnswerDTO> quidAnswersDTO) {
		List<Answer> answersResult = new ArrayList<Answer>();
		for(QuidAnswerDTO quidAnswerDTO : quidAnswersDTO) {
			answersResult.add(convertToAnswer(quidAnswerDTO));
		}
		return answersResult;
	}
	
	public static Answer convertToAnswer(QuidAnswerDTO quidAnswerDTO) {
		Answer answerResult = new Answer();
		answerResult.setId(quidAnswerDTO.getLabel());
		answerResult.setTitle(quidAnswerDTO.getTitle());
		return answerResult;
	}
	
}
