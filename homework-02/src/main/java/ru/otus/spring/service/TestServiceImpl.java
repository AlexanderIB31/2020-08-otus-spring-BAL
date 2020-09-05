package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.dao.QuestionReader;
import ru.otus.spring.dao.TextWriter;
import ru.otus.spring.domain.Question;

import java.util.List;

@Service("testService")
public class TestServiceImpl implements TestService {
    private final Integer threshold;
    private final QuestionReader questionReader;
    private final TextWriter textWriter;
    private final QuestionService questionService;

    public TestServiceImpl(Integer threshold, QuestionReader questionReader, QuestionService questionService, TextWriter textWriter) {
        this.threshold = threshold;
        this.questionReader = questionReader;
        this.questionService = questionService;
        this.textWriter = textWriter;
    }

    @Override
    public void run() {
        List<Question> questions = questionReader.read();
        questions.forEach(questionService::makeAnswerFor);

        if (isTestPassed(questions)) {
            textWriter.print("Test has been being passed");
        } else {
            textWriter.print("Test has not been being passed, try again.");
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
