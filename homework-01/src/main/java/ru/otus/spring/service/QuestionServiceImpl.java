package ru.otus.spring.service;

import ru.otus.spring.domain.Question;

import java.util.Scanner;

public class QuestionServiceImpl implements QuestionService {

    @Override
    public Question makeAnswerFor(Question question) {
        Scanner scanner = new Scanner(System.in);
        String userAnswer = scanner.nextLine();
        question.setUserAnswer(userAnswer);

        return question;
    }

    @Override
    public void printQuestion(Question question) {
        System.out.println("Question: " + question.getText());

        for (int i = 1; i <= question.getAnswers().size(); ++i) {
            System.out.println(i + ". " + question.getAnswers().get(i - 1));
        }
    }
}
