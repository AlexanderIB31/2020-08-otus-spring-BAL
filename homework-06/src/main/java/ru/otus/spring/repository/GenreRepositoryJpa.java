package ru.otus.spring.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@SuppressWarnings({"ConstantConditions", "SqlDialectInspection"})
@Repository
@Slf4j
public class GenreRepositoryJpa implements GenreRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long count() {
        return em.createQuery("select count(g) from Genre g", Long.class).getSingleResult();
    }

    @Override
    public void insert(Genre genre) {
        em.persist(genre);
        em.flush();
    }

    @Override
    public Genre getById(long id) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.id = :id", Genre.class);
        query.setParameter("id", id);

        return getGenreOrNullIfNotFound(query);
    }

    @Override
    public Genre getByName(String name) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.name = :name", Genre.class);
        query.setParameter("name", name);

        return getGenreOrNullIfNotFound(query);
    }

    @Override
    public List<Genre> getAll() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from Genre g where g.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void update(Genre genre) {
        em.merge(genre);
    }

    private Genre getGenreOrNullIfNotFound(TypedQuery<Genre> query) {
        List<Genre> result = query.getResultList();
        if (result == null || result.isEmpty()) return null;

        return result.get(0);
    }
}
