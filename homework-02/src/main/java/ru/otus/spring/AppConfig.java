package ru.otus.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import ru.otus.spring.dao.*;
import ru.otus.spring.service.QuestionService;
import ru.otus.spring.service.QuestionServiceImpl;
import ru.otus.spring.service.TestService;
import ru.otus.spring.service.TestServiceImpl;

import java.io.InputStream;

@EnableAspectJAutoProxy
@ComponentScan
@Configuration
@PropertySource(value = "classpath:application.properties")
public class AppConfig {

    @Bean("testReader")
    public InputStream reader(@Value("${question.uri}") String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    @Bean
    public TextWriter textWriter() {
        return new TextWriterConsole();
    }

    @Bean
    public AnswerReader answerReader() {
        return new AnswerReaderConsole();
    }

    @Bean
    public QuestionService questionService(TextWriter textWriter, AnswerReader answerReader) {
        return new QuestionServiceImpl(textWriter, answerReader);
    }

    @Bean
    public QuestionReader questionReader(InputStream testReader) {
        return new QuestionReaderFile(testReader);
    }

    @Bean
    public TestService testService(@Value("${question.threshold}") Integer threshold,
                                   QuestionReader questionReader, QuestionService questionService, TextWriter textWriter) {
        return new TestServiceImpl(threshold, questionReader, questionService, textWriter);
    }
}
