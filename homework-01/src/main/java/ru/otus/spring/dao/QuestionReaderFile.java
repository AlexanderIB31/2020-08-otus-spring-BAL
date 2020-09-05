package ru.otus.spring.dao;

import ru.otus.spring.domain.Question;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class QuestionReaderFile implements QuestionReader {
    private final String resourceName;
    private static final String COMMA_DELIMITER = ",";

    public QuestionReaderFile(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public List<Question> read() throws IOException {
        List<Question> result = new ArrayList<>();

        InputStream reader = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
        try (Scanner row = new Scanner(reader)) {
            while (row.hasNextLine()) {
                List<String> values = Arrays.asList(row.nextLine().split(COMMA_DELIMITER));

                Question newQuestion = new Question(values.get(0));
                newQuestion.setAnswers(values.stream().skip(1).filter(Objects::nonNull).collect(Collectors.toList()));
                result.add(newQuestion);
            }
        }

        return result;
    }
}
