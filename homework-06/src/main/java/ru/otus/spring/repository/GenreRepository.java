package ru.otus.spring.repository;

import ru.otus.spring.domain.Genre;

import java.util.List;

public interface GenreRepository {

    long count();

    void insert(Genre genre);

    Genre getById(long id);

    Genre getByName(String name);

    List<Genre> getAll();

    void deleteById(long id);

    void update(Genre genre);
}
