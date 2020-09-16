package ru.otus.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class QuestionReaderFile implements QuestionReader {
    private final Reader readerFile;
    private static final String COMMA_DELIMITER = ",";

    @Autowired
    public QuestionReaderFile(Reader readerFile) {
        this.readerFile = readerFile;
    }

    @Override
    public List<Question> read() throws IOException {
        List<Question> result = new ArrayList<>();

        while (readerFile.hasNextLine()) {
            List<String> values = Arrays.asList(readerFile.readLine().split(COMMA_DELIMITER));

            Question newQuestion = new Question(values.get(0));
            newQuestion.setAnswers(values
                    .stream()
                    .skip(1)
                    .filter(Objects::nonNull)
                    .map(x -> new Answer(Integer.parseInt(x)))
                    .collect(Collectors.toList()));
            result.add(newQuestion);
        }

        return result;
    }
}
