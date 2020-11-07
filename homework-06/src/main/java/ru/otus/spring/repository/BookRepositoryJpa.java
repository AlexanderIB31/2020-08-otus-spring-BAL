package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Book;

import javax.persistence.*;
import java.util.List;

@SuppressWarnings({"ConstantConditions", "SqlDialectInspection"})
@Repository
@Slf4j
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public long count() {
        return em.createQuery("select count(b) from Book b", Long.class).getSingleResult();
    }

    @Override
    public void insert(Book book) {
        em.persist(book);
        em.flush();
    }

    @Override
    public Book getById(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-entity-graph");
        TypedQuery<Book> query = em.createQuery("select b from Book b join fetch b.author join fetch b.genre where b.id = :id", Book.class);
        query.setParameter("id", id);
        query.setHint("javax.persistence.fetchgraph", entityGraph);

        return getBookOrNullIfNotFound(query);
    }

    @Override
    public List<Book> getByName(String name) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-entity-graph");
        TypedQuery<Book> query = em.createQuery("select b from Book b join fetch b.author join fetch b.genre where b.name = :name", Book.class);
        query.setParameter("name", name);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public List<Book> getAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-entity-graph");
        TypedQuery<Book> query = em.createQuery("select b from Book b join fetch b.author join fetch b.genre", Book.class);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from Book b where b.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void update(Book newBook) {
        em.merge(newBook);
    }

    private Book getBookOrNullIfNotFound(TypedQuery<Book> query) {
        List<Book> result = query.getResultList();
        if (result == null || result.isEmpty()) return null;

        return result.get(0);
    }
}
