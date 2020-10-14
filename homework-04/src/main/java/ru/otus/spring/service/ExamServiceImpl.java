package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.Props;
import ru.otus.spring.dao.TextWriter;
import ru.otus.spring.domain.Question;
import ru.otus.spring.localization.Translator;

import java.util.ListIterator;

@Service
public class ExamServiceImpl implements ExamService {
    private final TextWriter textWriter;
    private final QuestionService questionService;
    private final Translator translator;
    private final Props props;
    private ListIterator<Question> questionIterator;
    private Question currentQuestion;

    public ExamServiceImpl(QuestionService questionService, TextWriter textWriter, Translator translator, Props props) {
        this.questionService = questionService;
        this.textWriter = textWriter;
        this.translator = translator;
        this.props = props;
    }

    @Override
    public boolean isTestPassed() {
        return questionService.getQuestions()
                .stream()
                .map(q -> q.getCorrectAnswer().equals(q.getUserAnswer()))
                .filter(a -> a.equals(true))
                .count() >= props.getThreshold();
    }

    @Override
    public void run() {
        questionIterator = questionService.getQuestions().listIterator();
        textWriter.print(translator.getMessage("test.started"));
    }

    @Override
    public void printQuestion() {
        if (currentQuestion == null) return;

        textWriter.print(Math.min(questionIterator.nextIndex(), questionService.getQuestions().size()) + " of " + questionService.getQuestions().size());
        questionService.askQuestion(currentQuestion);

        if (currentQuestion.getUserAnswer() != null) {
            textWriter.print("Your answer is " + currentQuestion.getUserAnswer().getValue()  + ".");
        }
    }

    @Override
    public boolean nextQuestion() {
        if (questionIterator.hasNext()) {
            currentQuestion = questionIterator.next();

            return true;
        }

        return false;
    }

    @Override
    public boolean prevQuestion() {
        if (questionIterator.hasPrevious()) {
            currentQuestion = questionIterator.previous();

            return true;
        }

        return false;
    }

    @Override
    public void makeAnswer() {
        Question question = questionIterator.previous();
        questionIterator.set(questionService.makeAnswerFor(question));
        questionIterator.next();
    }

    @Override
    public void commit() {
        if (isTestPassed()) {
            textWriter.print(translator.getMessage("test.passed"));
        } else {
            textWriter.print(translator.getMessage("test.notPassed"));
        }
    }
}
