package ru.otus.spring.repository;

import ru.otus.spring.domain.Comment;

import java.util.List;

public interface CommentRepository {

    long count();

    void insert(Comment comment);

    Comment getById(long id);

    List<Comment> getAll();

    void deleteById(long id);

    void update(Comment comment);
}
