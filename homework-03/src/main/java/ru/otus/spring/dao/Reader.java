package ru.otus.spring.dao;

import java.io.IOException;

public interface Reader {

    Integer readInt();

    String readLine() throws IOException;

    boolean hasNextLine() throws IOException;
}
