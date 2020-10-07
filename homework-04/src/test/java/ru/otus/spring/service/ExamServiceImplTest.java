package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import ru.otus.spring.Props;
import ru.otus.spring.QuestionsBuilder;
import ru.otus.spring.dao.TextWriter;
import ru.otus.spring.localization.PropsTranslator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(
    properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
    }
)
@DisplayName("Тестируем сервис ExamServiceImpl")
public class ExamServiceImplTest {

    @MockBean
    private Props props;

    @MockBean
    private PropsTranslator translator;

    @MockBean
    private QuestionService questionService;

    @MockBean
    private TextWriter textWriter;

    @Autowired
    private ExamService examService;


    @DisplayName("когда количество правильных ответов больше или равно пороговому значению, то тест считается пройденным")
    @Test
    public void whenQuestionsCountCorrectAnswerMoreThanOrEqualThresholdValue_shouldIsTestPassedReturnTrue() {
        long countCorrectedAnswers = 3;
        var questions = QuestionsBuilder.create().addQuestionWithCorrectUserAnswer(countCorrectedAnswers).build();
        when(questionService.getQuestions()).thenReturn(questions);
        when(props.getThreshold()).thenReturn(countCorrectedAnswers);

        var isTestPassed = examService.isTestPassed();

        assertThat(isTestPassed).isTrue();
    }

    @DisplayName("когда количество правильных ответов меньше порогового значения, то тест считается не пройденным")
    @Test
    public void whenQuestionsCountCorrectAnswerLessThanThresholdValue_shouldIsTestPassedReturnFalse() {
        long countCorrectedAnswers = 3;
        var questions = QuestionsBuilder.create().addQuestionWithCorrectUserAnswer(countCorrectedAnswers - 1).build();
        when(questionService.getQuestions()).thenReturn(questions);
        when(props.getThreshold()).thenReturn(countCorrectedAnswers);

        var isTestPassed = examService.isTestPassed();

        assertThat(isTestPassed).isFalse();
    }

}
