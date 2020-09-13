package ru.otus.spring.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

@Component
@Scope("prototype")
public class ReaderFile implements Reader {
    private final String testPath;
    private long lineRead;

    public ReaderFile(@Value("${question.uri}") String testPath) {
        this.testPath = testPath;
        this.lineRead = 0;
    }

    @Override
    public Integer readInt() {
        return null;
    }

    @Override
    public String readLine() throws IOException {
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(testPath)) {
            try (Scanner scanner = new Scanner(Objects.requireNonNull(stream))) {
                skipReadLines(scanner);
                String line = scanner.nextLine();
                lineRead++;
                return line;
            }
        }
    }

    @Override
    public boolean hasNextLine() throws IOException {
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(testPath)) {
            try (Scanner scanner = new Scanner(Objects.requireNonNull(stream))) {
                skipReadLines(scanner);
                return scanner.hasNextLine();
            }
        }
    }


    private void skipReadLines(Scanner scanner) {
        for (long i = 0; i < lineRead && scanner.hasNextLine(); ++i) {
            scanner.nextLine();
        }
    }
}
