package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.Reader;
import ru.otus.spring.dao.TextWriter;
import ru.otus.spring.domain.Question;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final TextWriter textWriter;
    private final Reader readerConsole;

    @Autowired
    public QuestionServiceImpl(TextWriter textWriter, Reader readerConsole) {
        this.textWriter = textWriter;
        this.readerConsole = readerConsole;
    }

    @Override
    public Question makeAnswerFor(Question question) {
        textWriter.print("Question: " + question.getText());
        for (int i = 1; i <= question.getAnswers().size(); ++i) {
            textWriter.print(i + ". " + question.getAnswers().get(i - 1).getValue());
        }
        question.setUserAnswer(question.getAnswers().get(readerConsole.readInt()));

        return question;
    }
}
