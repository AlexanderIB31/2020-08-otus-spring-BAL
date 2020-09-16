package ru.otus.spring.domain;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private final String text;
    private List<Answer> answers = new ArrayList<>();
    private Answer userAnswer;

    public Question(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void setUserAnswer(Answer userAnswer) {
        this.userAnswer = userAnswer;
    }

    public Answer getUserAnswer() {
        return userAnswer;
    }

    public Answer getCorrectAnswer() {
        return answers.get(0);
    }
}
