package ru.otus.spring.dao;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Scanner;

@Component
public class ReaderConsole implements Reader {

    @Override
    public Integer readInt() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    @Override
    public String readLine() {
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextLine();
        }
    }

    @Override
    public boolean hasNextLine() throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.hasNextLine();
        }
    }
}
