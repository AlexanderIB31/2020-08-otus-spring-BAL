package ru.otus.spring.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.BooksRef;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"ConstantConditions", "SqlDialectInspection"})
@Repository
@Slf4j
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final BookDao bookDaoJdbc;

    public AuthorDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations, @Lazy BookDao bookDaoJdbc)
    {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.bookDaoJdbc = bookDaoJdbc;
    }

    @Override
    public int count() {
        return namedParameterJdbcOperations.queryForObject("select count(*) from authors", new HashMap<>(), Integer.class);
    }

    @Override
    public void insert(Author author) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", author.getId());
            params.put("name", author.getName());
            namedParameterJdbcOperations.update(
                    "insert into authors (id, `name`) values (:id, :name)", params
            );
        }
        catch (EmptyResultDataAccessException e) {
            log.warn("Error in insert method", e);
        }
    }

    @Override
    public Author getById(long id) {
        try {
            Map<String, Object> params = Collections.singletonMap("id", id);
            Author author = namedParameterJdbcOperations.queryForObject(
                    "select id, name from authors where id = :id", params, new AuthorMapper()
            );
            BooksRef booksRef = new BooksRef(bookDaoJdbc::getByAuthorIds, Collections.singletonList(author.getId()));
            author.setBooksRef(booksRef);
            return author;
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Author getByName(String name) {
        try {
            Map<String, Object> params = Collections.singletonMap("name", name);
            Author author = namedParameterJdbcOperations.queryForObject(
                    "select id, name from authors where name = :name", params, new AuthorMapper()
            );
            BooksRef booksRef = new BooksRef(bookDaoJdbc::getByAuthorIds, Collections.singletonList(author.getId()));
            author.setBooksRef(booksRef);
            return author;
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Author> getAll() {
        try {
            List<Author> authors = namedParameterJdbcOperations.query("select id, name from authors", new AuthorMapper());
            BooksRef booksRef = new BooksRef(bookDaoJdbc::getByAuthorIds, authors.stream().map(Author::getId).collect(Collectors.toList()));
            authors.forEach(a -> a.setBooksRef(booksRef));
            return authors;
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void deleteById(long id) {
        try {
            Map<String, Object> params = Collections.singletonMap("id", id);
            namedParameterJdbcOperations.update(
                    "delete from authors where id = :id", params
            );
        }
        catch (EmptyResultDataAccessException e) {
            log.warn("Error in deleteById method", e);
        }
    }

    @Override
    public void updateById(long id, Author newAuthor) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            params.put("name", newAuthor.getName());
            namedParameterJdbcOperations.update(
                    "update authors set name = :name where id = :id", params
            );
        }
        catch (EmptyResultDataAccessException e) {
            log.warn("Error in updateById method", e);
        }
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            return new Author(id, name);
        }
    }
}
