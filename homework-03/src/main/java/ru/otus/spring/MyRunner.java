package ru.otus.spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.spring.service.ExamService;

@Component
public class MyRunner implements CommandLineRunner {
    private final ExamService examService;

    public MyRunner(ExamService examService) {
        this.examService = examService;
    }

    @Override
    public void run(String... args) {
        examService.run();
    }
}
