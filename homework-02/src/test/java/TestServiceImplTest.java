import org.junit.jupiter.api.Test;
import ru.otus.spring.dao.QuestionReader;
import ru.otus.spring.dao.TextWriter;
import ru.otus.spring.service.QuestionService;
import ru.otus.spring.service.TestServiceImpl;

import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class TestServiceImplTest {

    @Test
    public void whenQuestionsContain3CorrectAnswerAndThresholdEqual3_shoudlIsTestPassedReturnTrue() {
        var questionReader = mock(QuestionReader.class);
        var questionService = mock(QuestionService.class);
        var textWriter = mock(TextWriter.class);
        var testService = new TestServiceImpl(3, questionReader, questionService, textWriter);
        var questions = QuestionsBuilder.create().addQuestionWithCorrectUserAnswer(3).build();

        var isTestPassed = testService.isTestPassed(questions);

        assertTrue(isTestPassed);
    }

}
