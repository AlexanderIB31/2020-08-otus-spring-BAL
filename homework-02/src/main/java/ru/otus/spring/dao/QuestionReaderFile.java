package ru.otus.spring.dao;

import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class QuestionReaderFile implements QuestionReader {
    private final InputStream testReader;
    private static final String COMMA_DELIMITER = ",";

    public QuestionReaderFile(InputStream testReader) {
        this.testReader = testReader;
    }

    @Override
    public List<Question> read() {
        List<Question> result = new ArrayList<>();

        try (Scanner row = new Scanner(Objects.requireNonNull(testReader))) {
            while (row.hasNextLine()) {
                List<String> values = Arrays.asList(row.nextLine().split(COMMA_DELIMITER));

                Question newQuestion = new Question(values.get(0));
                newQuestion.setAnswers(values
                        .stream()
                        .skip(1)
                        .filter(Objects::nonNull)
                        .map(x -> {
                            var a = new Answer();
                            a.setValue(Integer.parseInt(x));
                            return a;
                        })
                        .collect(Collectors.toList()));
                result.add(newQuestion);
            }
        }

        return result;
    }
}
