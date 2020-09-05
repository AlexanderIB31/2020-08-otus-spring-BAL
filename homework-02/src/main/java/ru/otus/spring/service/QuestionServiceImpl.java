package ru.otus.spring.service;

import ru.otus.spring.dao.AnswerReader;
import ru.otus.spring.domain.Question;

import java.io.PrintStream;

public class QuestionServiceImpl implements QuestionService {


    private final PrintStream printStream;
    private final AnswerReader answerReader;

    public QuestionServiceImpl(PrintStream printStream, AnswerReader answerReader) {
        this.printStream = printStream;
        this.answerReader = answerReader;
    }

    @Override
    public Question makeAnswerFor(Question question) {
        printStream.println("Question: " + question.getText());
        for (int i = 1; i <= question.getAnswers().size(); ++i) {
            printStream.println(i + ". " + question.getAnswers().get(i - 1).getValue());
        }
        question.setUserAnswer(answerReader.read(question));

        return question;
    }
}
