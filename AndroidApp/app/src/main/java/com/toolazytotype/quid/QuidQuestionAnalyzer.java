package com.toolazytotype.quid;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.toolazytotype.Action;
import com.toolazytotype.TextAnalyzer;
import com.toolazytotype.quid.dto.QuidAnswerDTO;
import com.toolazytotype.quid.dto.QuidQuestionDTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuidQuestionAnalyzer extends TextAnalyzer {

    public static final int NONE = 0;
    public static final int QUESTION_LABEL_DETECTED = 0x01;
    public static final int QUESTION_TITLE_DETECTED = 0x02;
    public static final int ANSWERS_LABEL_DETECTED = 0x04;
    public static final int LAST_ANSWER_TITLE_DETECTED = 0x08;
    public static final int QUESTION_OPEN_LABEL_DETECTED = 0x10;

    private static final String LOG_TAG = "QuidQuestionAnalyzer";
    private static final String REG_QUESTION_LABEL = "Q\\.\\s[0-9]+$";
    private static final String REG_IGNORED = "([0-9]+:[0-9]+:[0-9]+)|(^[0-9]+s$)";
    private static final String REG_ANSWER_LABEL = "^[ABCD]$";
    private static final String REG_OPEN_QUESTION_LABEL = "Q. 11";

    private static final int MAX_ANSWERS_COUNT = 4;
    private static Pattern patternQuestion;
    private static Pattern patternIgnoredLabel;
    private static Pattern patternAnswerLabel;
    private static Matcher matcher;
    private static Gson gson;

    private int detectionStatus; // Bitmask
    private String textToAnalyse;
    private String lastQuestionLabel;
    private String lastAnswerLabel;

    private QuidQuestionDTO quidQuestionDTO;

    public QuidQuestionAnalyzer(Action action) {
        super(action);
        patternQuestion = Pattern.compile(REG_QUESTION_LABEL);
        patternIgnoredLabel = Pattern.compile(REG_IGNORED);
        patternAnswerLabel = Pattern.compile(REG_ANSWER_LABEL);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.setPrettyPrinting().create();
    }

    @Override
    public void analyze(String text) {
        this.textToAnalyse = text;
        if (!mustBeIgnored()) {
            doDetection();
            if (isDetectionsPassed(QUESTION_LABEL_DETECTED, QUESTION_TITLE_DETECTED, LAST_ANSWER_TITLE_DETECTED) || isDetectionsPassed(QUESTION_OPEN_LABEL_DETECTED, QUESTION_TITLE_DETECTED)) {
                String questionContextJson = gson.toJson(quidQuestionDTO);
                action.trigger(questionContextJson);
                detectionStatus = NONE;
            }
        }
    }

    private void doDetection() {
        if (doLabelDetection()) {
            addDetectionStatus(QUESTION_LABEL_DETECTED);
            if (isOpenQuestion())
                addDetectionStatus(QUESTION_OPEN_LABEL_DETECTED);
        }
        else if (doQuestionTitleDetection())
            addDetectionStatus(QUESTION_TITLE_DETECTED);
        else if (doAnswerLabelDetection())
            addDetectionStatus(ANSWERS_LABEL_DETECTED);
        else if (doAnswerTitleDetection()) {
            removeDetectionStatus(ANSWERS_LABEL_DETECTED);
            if (isLastAnswerTitle())
                addDetectionStatus(LAST_ANSWER_TITLE_DETECTED);
        }
    }

    private boolean isOpenQuestion() {
        /** Open choice question item does not need an answer items parsing **/
        return lastQuestionLabel.equals(REG_OPEN_QUESTION_LABEL);
    }

    private boolean isLastAnswerTitle() {
        return quidQuestionDTO.getAnswers().size() >= MAX_ANSWERS_COUNT;
    }

    private boolean doLabelDetection() {
        if (isQuestionLabel() && !isDetectionPassed(QUESTION_LABEL_DETECTED) && notEqualsToLastLabel()) {
            quidQuestionDTO = new QuidQuestionDTO();
            lastQuestionLabel = textToAnalyse;
            return true;
        }
        return false;
    }

    private boolean doQuestionTitleDetection() {
        if (isDetectionPassed(QUESTION_LABEL_DETECTED) && !isDetectionPassed(QUESTION_TITLE_DETECTED)) {
            quidQuestionDTO.setQuestionEntitled(textToAnalyse);
            Log.d(LOG_TAG, String.format("Question Title detected : %s", textToAnalyse));
            return true;
        }
        return false;
    }

    private boolean doAnswerLabelDetection() {
        if (isDetectionsPassed(QUESTION_LABEL_DETECTED, QUESTION_TITLE_DETECTED) && isAnswerLabel()) {
            lastAnswerLabel = textToAnalyse;
            Log.d(LOG_TAG, String.format("Answer Label detected : %s", textToAnalyse));
            return true;
        }
        return false;
    }

    private boolean doAnswerTitleDetection() {
        if (isDetectionsPassed(QUESTION_LABEL_DETECTED, QUESTION_TITLE_DETECTED, ANSWERS_LABEL_DETECTED)) {
            QuidAnswerDTO answerDTO = new QuidAnswerDTO(lastAnswerLabel, textToAnalyse);
            quidQuestionDTO.getAnswers().add(answerDTO);
            Log.d(LOG_TAG, String.format("Answer Title detected : %s", textToAnalyse));
            return true;
        }
        return false;
    }

    private boolean notEqualsToLastLabel() {
        /** Does not want to trigger twice for same question **/
        return !textToAnalyse.equals(lastQuestionLabel);
    }

    private boolean mustBeIgnored() {
        matcher = patternIgnoredLabel.matcher(textToAnalyse);
        if (matcher.find()) {
            Log.v(LOG_TAG, String.format("String ignored : %s", textToAnalyse));
            return true;
        }
        return false;
    }

    private boolean isQuestionLabel() {
        matcher = patternQuestion.matcher(textToAnalyse);
        return matcher.find();
    }

    private boolean isAnswerLabel() {
        matcher = patternAnswerLabel.matcher(textToAnalyse);
        return matcher.find();
    }

    private boolean isDetectionPassed(int detectionStatusStep) {
        return (detectionStatus & detectionStatusStep) > 0;
    }

    private boolean isDetectionsPassed(int... detectionStatusSteps) {
        for (int detectionStatusStep : detectionStatusSteps) {
            if (!isDetectionPassed(detectionStatusStep))
                return false;
        }
        return true;
    }

    private void addDetectionStatus(int statusToAdd) {
        detectionStatus = detectionStatus | statusToAdd;
    }

    private void removeDetectionStatus(int statusToRemove) {
        if ((detectionStatus & statusToRemove) != 0)
            detectionStatus = detectionStatus ^ statusToRemove;
    }
}
