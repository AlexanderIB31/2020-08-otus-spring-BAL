package ru.otus.spring.dao;

import ru.otus.spring.domain.Book;

import java.util.List;

public interface BookDao {

    int count();

    void insert(Book book);

    Book getById(long id);

    List<Book> getByName(String name);

    List<Book> getAll();

    void deleteById(long id);

    void updateById(long id, Book newBook);

    List<Book> getByAuthorIds(List<Long> ids);

    List<Book> getByGenreIds(List<Long> ids);
}
