package ru.otus.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.dao.QuestionReader;
import ru.otus.spring.service.QuestionService;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        var context = new ClassPathXmlApplicationContext("spring-context.xml");
        var service = context.getBean(QuestionService.class);
        var reader = context.getBean(QuestionReader.class);
        reader.read().forEach(q -> {
            service.printQuestion(q);
            service.makeAnswerFor(q);
        });
    }
}
