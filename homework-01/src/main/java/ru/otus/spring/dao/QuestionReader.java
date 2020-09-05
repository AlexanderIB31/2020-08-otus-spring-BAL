package ru.otus.spring.dao;

import ru.otus.spring.domain.Question;

import java.io.IOException;
import java.util.List;

public interface QuestionReader {
    List<Question> read() throws IOException;
}
