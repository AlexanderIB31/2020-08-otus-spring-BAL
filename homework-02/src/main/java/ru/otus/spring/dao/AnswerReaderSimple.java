package ru.otus.spring.dao;

import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;

import java.io.InputStream;
import java.util.Scanner;

public class AnswerReaderSimple implements AnswerReader {
    private final InputStream inputStream;

    public AnswerReaderSimple(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public Answer read(Question question) {
        Scanner scanner = new Scanner(inputStream);

        int answerId = scanner.nextInt();
        return question.getAnswers().get(answerId - 1);
    }
}
