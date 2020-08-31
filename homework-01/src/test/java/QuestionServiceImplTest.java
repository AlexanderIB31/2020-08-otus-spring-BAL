import org.junit.jupiter.api.Test;
import ru.otus.spring.domain.Question;
import ru.otus.spring.service.QuestionService;
import ru.otus.spring.service.QuestionServiceImpl;

import static org.mockito.Mockito.*;

public class QuestionServiceImplTest {

    @Test
    public void whenQuestionHasNotAnswers_shouldPrintQuestionCalls1TimeGetAnswers() {
        Question spyQuestion = spy(new Question(""));
        QuestionService service = new QuestionServiceImpl();

        service.printQuestion(spyQuestion);

        verify(spyQuestion, times(1)).getAnswers();
    }

}
