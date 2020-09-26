package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.aop.Loggable;
import ru.otus.spring.dao.QuestionReader;
import ru.otus.spring.dao.Reader;
import ru.otus.spring.dao.TextWriter;
import ru.otus.spring.domain.Question;
import ru.otus.spring.localization.Translator;

import java.io.IOException;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final TextWriter textWriter;
    private final Reader readerConsole;
    private final Translator translator;

    private final List<Question> questions;

    public QuestionServiceImpl(TextWriter textWriter, Reader readerConsole, Translator translator, QuestionReader questionReader) throws IOException {
        this.textWriter = textWriter;
        this.readerConsole = readerConsole;
        this.translator = translator;

        questions = questionReader.read();
    }

    @Override
    @Loggable
    public Question makeAnswerFor(Question question) {
        textWriter.print(translator.getMessage(question.getText()));
        for (int i = 1; i <= question.getAnswers().size(); ++i) {
            textWriter.print(i + ". " + question.getAnswers().get(i - 1).getValue());
        }
        question.setUserAnswer(question.getAnswers().get(readerConsole.readInt() - 1));

        return question;
    }

    @Override
    public List<Question> getQuestions() {
        return questions;
    }
}
