import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class QuestionsBuilder {
    private final List<Question> questions = new ArrayList<>();

    public static QuestionsBuilder create() {
        return new QuestionsBuilder();
    }

    public List<Question> build() {
        return questions;
    }

    public QuestionsBuilder addQuestionWithCorrectUserAnswer(Integer count) {
        IntStream.range(0, count).forEach(i -> this.addQuestionWithCorrectUserAnswer());

        return this;
    }

    public QuestionsBuilder addQuestionWithCorrectUserAnswer() {
        Question question = new Question("");
        Answer answer = new Answer(0);
        question.getAnswers().add(answer);
        question.setUserAnswer(answer);

        questions.add(question);

        return this;
    }
}
