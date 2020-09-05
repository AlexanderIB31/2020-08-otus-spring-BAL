package ru.otus.spring.dao;

public class TextWriterConsole implements TextWriter {

    @Override
    public void print(String text) {
        System.out.println(text);
    }
}
