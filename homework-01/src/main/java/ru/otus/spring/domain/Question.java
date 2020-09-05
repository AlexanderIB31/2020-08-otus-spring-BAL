package ru.otus.spring.domain;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private final String text;
    private List<String> answers = new ArrayList<>();
    private String userAnswer;

    public Question(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }
}
