package ru.otus.spring.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@SuppressWarnings({"ConstantConditions", "SqlDialectInspection"})
@Repository
@Slf4j
public class AuthorRepositoryJpa implements AuthorRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long count() {
        return em.createQuery("select count(a) from Author a", Long.class).getSingleResult();
    }

    @Override
    public void insert(Author author) {
        em.persist(author);
        em.flush();
    }

    @Override
    public Author getById(long id) {
        TypedQuery<Author> query = em.createQuery("select a from Author a where a.id = :id", Author.class);
        query.setParameter("id", id);

        return getAuthorOrNullIfNotFound(query);
    }

    @Override
    public Author getByName(String name) {
        TypedQuery<Author> query = em.createQuery("select a from Author a where a.name = :name", Author.class);
        query.setParameter("name", name);

        return getAuthorOrNullIfNotFound(query);
    }

    @Override
    public List<Author> getAll() {
        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from Author a where a.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void update(Author newAuthor) {
        em.merge(newAuthor);
    }

    private Author getAuthorOrNullIfNotFound(TypedQuery<Author> query) {
        List<Author> result = query.getResultList();
        if (result == null || result.isEmpty()) return null;

        return result.get(0);
    }
}
