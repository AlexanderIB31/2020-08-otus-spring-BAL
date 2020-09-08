package ru.otus.spring.dao;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;

import java.util.Scanner;

@Component
public class AnswerReaderConsole implements AnswerReader {
    @Override
    public Answer read(Question question) {
        Scanner scanner = new Scanner(System.in);

        int answerId = scanner.nextInt();
        return question.getAnswers().get(answerId - 1);
    }
}
