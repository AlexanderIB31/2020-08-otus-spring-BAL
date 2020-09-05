package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.dao.AnswerReader;
import ru.otus.spring.dao.TextWriter;
import ru.otus.spring.domain.Question;

@Service("questionService")
public class QuestionServiceImpl implements QuestionService {
    private final TextWriter textWriter;
    private final AnswerReader answerReader;

    public QuestionServiceImpl(TextWriter textWriter, AnswerReader answerReader) {
        this.textWriter = textWriter;
        this.answerReader = answerReader;
    }

    @Override
    public Question makeAnswerFor(Question question) {
        textWriter.print("Question: " + question.getText());
        for (int i = 1; i <= question.getAnswers().size(); ++i) {
            textWriter.print(i + ". " + question.getAnswers().get(i - 1).getValue());
        }
        question.setUserAnswer(answerReader.read(question));

        return question;
    }
}
