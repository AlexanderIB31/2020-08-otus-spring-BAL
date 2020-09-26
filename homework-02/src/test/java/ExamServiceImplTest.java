import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.otus.spring.dao.QuestionReader;
import ru.otus.spring.dao.TextWriter;
import ru.otus.spring.service.ExamServiceImpl;
import ru.otus.spring.service.QuestionService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ExamServiceImplTest {

    @Mock
    private QuestionReader questionReader;

    @Mock
    private QuestionService questionService;

    @Mock
    private TextWriter textWriter;

    @Test
    public void whenQuestionsCountCorrectAnswerEqualThresholdValue_shoudIsTestPassedReturnTrue() {
        int correctAnswer = 3;
        var examService = new ExamServiceImpl(correctAnswer, questionReader, questionService, textWriter);
        var questions = QuestionsBuilder.create().addQuestionWithCorrectUserAnswer(correctAnswer).build();

        var isTestPassed = examService.isTestPassed(questions);

        assertTrue(isTestPassed);
    }

    @Test
    public void whenQuestionsCountCorrectAnswerNotEqualThresholdValue_shoudIsTestPassedReturnFalse() {
        int correctAnswer = 3;
        var examService = new ExamServiceImpl(correctAnswer, questionReader, questionService, textWriter);
        var questions = QuestionsBuilder.create().addQuestionWithCorrectUserAnswer(correctAnswer - 1).build();

        var isTestPassed = examService.isTestPassed(questions);

        assertFalse(isTestPassed);
    }

}
