package ru.otus.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.spring.service.ExamService;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        var examService = context.getBean(ExamService.class);
        examService.run();
    }
}
