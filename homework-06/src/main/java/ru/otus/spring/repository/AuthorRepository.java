package ru.otus.spring.repository;

import ru.otus.spring.domain.Author;

import java.util.List;

public interface AuthorRepository {

    long count();

    void insert(Author author);

    Author getById(long id);

    Author getByName(String name);

    List<Author> getAll();

    void deleteById(long id);

    void update(Author newAuthor);
}
