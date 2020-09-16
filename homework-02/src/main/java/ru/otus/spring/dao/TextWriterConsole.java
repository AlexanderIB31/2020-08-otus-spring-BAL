package ru.otus.spring.dao;

import org.springframework.stereotype.Component;

@Component
public class TextWriterConsole implements TextWriter {

    @Override
    public void print(String text) {
        System.out.println(text);
    }
}
