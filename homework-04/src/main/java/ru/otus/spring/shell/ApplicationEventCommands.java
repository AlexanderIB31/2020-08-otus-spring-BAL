package ru.otus.spring.shell;


import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.service.ExamService;

@ShellComponent
public class ApplicationEventCommands {

    private final ExamService examService;

    public ApplicationEventCommands(ExamService examService) {
        this.examService = examService;
    }

    @ShellMethod(value = "Start exam", key = {"start"})
    public void start() {
        examService.run();
    }

    @ShellMethod(value = "Next question", key = {"next"})
    public void next() {
        examService.nextQuestion();
    }

    @ShellMethod(value = "Prev question", key = {"prev"})
    public void prev() {
        examService.prevQuestion();
    }

    @ShellMethod(value = "Print question", key = {"p", "print"})
    public void print() {
        examService.printQuestion();
    }

    @ShellMethod(value = "Make answer", key = {"a", "answer"})
    public void answer() {
        examService.makeAnswer();
    }

    @ShellMethod(value = "Commit exam", key = {"commit"})
    public void commit() {
        examService.commit();
    }
}
