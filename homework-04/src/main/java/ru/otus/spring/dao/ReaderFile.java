package ru.otus.spring.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.otus.spring.Props;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

@Component
@Scope("prototype")
public class ReaderFile implements Reader {
    private final Props props;
    private long lineRead;

    public ReaderFile(Props props) {
        this.props = props;;
        this.lineRead = 0;
    }

    @Override
    public Integer readInt() {
        return null;
    }

    @Override
    public String readLine() throws IOException {
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(props.getUri())) {
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
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(props.getUri())) {
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
