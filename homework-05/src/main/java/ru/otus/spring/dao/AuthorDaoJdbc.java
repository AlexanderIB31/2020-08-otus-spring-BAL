package ru.otus.spring.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
@Slf4j
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final BookDao bookDao;

    public AuthorDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations, BookDao bookDao)
    {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.bookDao = bookDao;
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
            return namedParameterJdbcOperations.queryForObject(
                    "select * from authors where id = :id", params, new AuthorMapper(bookDao)
            );
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Author getByName(String name) {
        try {
            Map<String, Object> params = Collections.singletonMap("name", name);
            return namedParameterJdbcOperations.queryForObject(
                    "select * from authors where name = :name", params, new AuthorMapper(bookDao)
            );
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Author> getAll() {
        try {
            return namedParameterJdbcOperations.query("select * from authors", new AuthorMapper(bookDao));
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
        private final BookDao bookDao;

        private AuthorMapper(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            List<Book> books = bookDao.getAll().stream().filter(x -> x.getAuthorId() == id).collect(Collectors.toList());
            Author author = new Author(id, name);
            author.setBooks(books);
            return author;
        }
    }
}
