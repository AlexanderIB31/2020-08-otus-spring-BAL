package ru.otus.spring.service;

import ru.otus.spring.domain.Question;

import java.util.List;

public interface QuestionService {

    void askQuestion(Question question);

    Question makeAnswerFor(Question question);

    List<Question> getQuestions();
}
