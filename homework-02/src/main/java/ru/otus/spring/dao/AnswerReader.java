package ru.otus.spring.dao;

import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;

public interface AnswerReader {
    Answer read(Question question);
}
