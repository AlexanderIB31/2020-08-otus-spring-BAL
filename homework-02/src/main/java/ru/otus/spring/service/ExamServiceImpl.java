package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.QuestionReader;
import ru.otus.spring.dao.TextWriter;
import ru.otus.spring.domain.Question;

import java.io.IOException;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {
    private final Integer threshold;
    private final QuestionReader questionReader;
    private final TextWriter textWriter;
    private final QuestionService questionService;

    @Autowired
    public ExamServiceImpl(@Value("${question.threshold}") Integer threshold, QuestionReader questionReader, QuestionService questionService, TextWriter textWriter) {
        this.threshold = threshold;
        this.questionReader = questionReader;
        this.questionService = questionService;
        this.textWriter = textWriter;
    }

    @Override
    public void run() throws IOException {
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
