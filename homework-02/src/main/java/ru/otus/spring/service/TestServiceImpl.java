package ru.otus.spring.service;

import ru.otus.spring.dao.QuestionReader;
import ru.otus.spring.domain.Question;

import java.io.PrintStream;
import java.util.List;

public class TestServiceImpl implements TestService {
    private final Integer threshold;
    private final QuestionReader questionReader;
    private final PrintStream resultWriter;
    private final QuestionService questionService;

    public TestServiceImpl(Integer threshold, QuestionReader questionReader, QuestionService questionService, PrintStream resultWriter) {
        this.threshold = threshold;
        this.questionReader = questionReader;
        this.questionService = questionService;
        this.resultWriter = resultWriter;
    }

    @Override
    public void run() {
        List<Question> questions = questionReader.read();
        questions.forEach(questionService::makeAnswerFor);

        if (isTestPassed(questions)) {
            resultWriter.println("Test has been being passed");
        } else {
            resultWriter.println("Test has not been being passed, try again.");
        }
    }

    @Override
    public boolean isTestPassed(List<Question> questions) {
        return questions
                .stream()
                .map(q -> q.getCorrectAnswer().equals(q.getUserAnswer()))
                .filter(a -> a.equals(true))
                .count() >= threshold;
    }
}
