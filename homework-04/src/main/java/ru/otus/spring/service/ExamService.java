package ru.otus.spring.service;

public interface ExamService {

    boolean isTestPassed();

    void run();

    void printQuestion();

    boolean nextQuestion();

    boolean prevQuestion();

    void makeAnswer();

    void commit();
}
