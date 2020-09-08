package ru.otus.spring.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class QuestionReaderFile implements QuestionReader {
    private final String testPath;
    private static final String COMMA_DELIMITER = ",";

    public QuestionReaderFile(@Value("${question.uri}") String testPath) {
        this.testPath = testPath;
    }

    @Override
    public List<Question> read() throws IOException {
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(testPath)) {
            List<Question> result = new ArrayList<>();

            try (Scanner row = new Scanner(Objects.requireNonNull(stream))) {
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
}
