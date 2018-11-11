package com.toolazytotype.quid.dto;

import java.util.ArrayList;
import java.util.List;

public class QuidQuestionDTO {

    private String questionEntitled;
    private List<QuidAnswerDTO> answers;

    public QuidQuestionDTO() {
        answers = new ArrayList<>();
    }

    public String getQuestionEntitled() {
        return questionEntitled;
    }

    public void setQuestionEntitled(String questionEntitled) {
        this.questionEntitled = questionEntitled;
    }

    public List<QuidAnswerDTO> getAnswers() {
        return answers;
    }
}
