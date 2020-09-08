package ru.otus.spring.service;

import ru.otus.spring.domain.Question;

import java.io.IOException;
import java.util.List;

public interface ExamService {

    void run() throws IOException;

    boolean isTestPassed(List<Question> questions);
}
