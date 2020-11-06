package ru.otus.spring.service;

import ru.otus.spring.domain.Book;

import java.util.List;

public interface BookService {
    Book createBook(String bookName, String authorName, String genreName);
    List<Book> deleteBookByName(String bookName);
    void updateBookByName(String bookName, String newName, String newAuthorName, String newGenreName);
    Book addComment4Book(String bookName, String comment);

    StringBuilder print();
}
