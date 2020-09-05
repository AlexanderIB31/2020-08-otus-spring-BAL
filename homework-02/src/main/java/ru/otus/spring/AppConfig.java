package ru.otus.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.spring.dao.AnswerReader;
import ru.otus.spring.dao.AnswerReaderSimple;
import ru.otus.spring.dao.QuestionReader;
import ru.otus.spring.dao.QuestionReaderFile;
import ru.otus.spring.service.QuestionService;
import ru.otus.spring.service.QuestionServiceImpl;
import ru.otus.spring.service.TestService;
import ru.otus.spring.service.TestServiceImpl;

import java.io.InputStream;
import java.io.PrintStream;

@Configuration
@PropertySource(value = "classpath:application.properties")
public class AppConfig {

    @Bean("testReader")
    public InputStream reader(@Value("${question.uri}") String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    @Bean
    public InputStream inputStream() {
        return System.in;
    }

    @Bean("resultWriter")
    public PrintStream writer() {
        return System.out;
    }

    @Bean
    public AnswerReader answerReader(InputStream inputStream) {
        return new AnswerReaderSimple(inputStream);
    }

    @Bean
    public QuestionService questionService(PrintStream resultWriter, AnswerReader answerReader) {
        return new QuestionServiceImpl(resultWriter, answerReader);
    }

    @Bean
    public QuestionReader questionReader(InputStream testReader) {
        return new QuestionReaderFile(testReader);
    }

    @Bean
    public TestService testService(@Value("${question.threshold}") Integer threshold, QuestionReader questionReader, QuestionService questionService, PrintStream resultWriter) {
        return new TestServiceImpl(threshold, questionReader, questionService, resultWriter);
    }
}
