package ru.otus.spring.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Comment;

import javax.persistence.*;
import java.util.List;

@Repository
@Slf4j
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long count() {
        return em.createQuery("select count(c) from Comment c", Long.class).getSingleResult();
    }

    @Override
    public void insert(Comment comment) {
        em.persist(comment);
    }

    @Override
    public Comment getById(long id) {
        TypedQuery<Comment> query = em.createQuery("select c from Comment c where c.id = :id", Comment.class);
        query.setParameter("id", id);

        List<Comment> result = query.getResultList();
        if (result == null || result.isEmpty()) return null;

        return result.get(0);
    }

    @Override
    public List<Comment> getAll() {
        TypedQuery<Comment> query = em.createQuery("select c from Comment c", Comment.class);
        return query.getResultList();
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from Comment c where c.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void update(Comment comment) {
        em.merge(comment);
    }
}
