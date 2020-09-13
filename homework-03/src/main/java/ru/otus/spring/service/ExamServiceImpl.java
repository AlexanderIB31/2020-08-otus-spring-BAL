package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.Props;
import ru.otus.spring.dao.TextWriter;
import ru.otus.spring.localization.Translator;

@Service
public class ExamServiceImpl implements ExamService {
    private final TextWriter textWriter;
    private final QuestionService questionService;
    private final Props props;
    private final Translator translator2Ru;

    public ExamServiceImpl(Props props, QuestionService questionService, TextWriter textWriter, Translator translator2Ru) {
        this.props = props;
        this.questionService = questionService;
        this.textWriter = textWriter;
        this.translator2Ru = translator2Ru;
    }

    @Override
    public boolean isTestPassed() {
        return questionService.getQuestions()
                .stream()
                .map(q -> q.getCorrectAnswer().equals(q.getUserAnswer()))
                .filter(a -> a.equals(true))
                .count() >= props.getThreshold();
    }

    @Override
    public void run() {
        questionService.getQuestions().forEach(questionService::makeAnswerFor);

        if (isTestPassed()) {
            textWriter.print(translator2Ru.getMessage("test.passed"));
        } else {
            textWriter.print(translator2Ru.getMessage("test.notPassed"));
        }
    }
}
