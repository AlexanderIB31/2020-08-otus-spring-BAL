package ru.otus.spring.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public BookDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations)
    {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public int count() {
        return namedParameterJdbcOperations.queryForObject("select count(*) from books", new HashMap<>(), Integer.class);
    }

    @Override
    public void insert(Book book) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", book.getId());
            params.put("name", book.getName());
            params.put("authorId", book.getAuthorId());
            params.put("genreId", book.getGenreId());
            namedParameterJdbcOperations.update("insert into books (id, `name`, `author_Id`, `genre_Id`) values (:id, :name, :authorId, :genreId)", params);
        }
        catch (EmptyResultDataAccessException e) {
            log.warn("Error in insert method", e);
        }
    }

    @Override
    public Book getById(long id) {
        try {
            Map<String, Object> params = Collections.singletonMap("id", id);
            return namedParameterJdbcOperations.queryForObject(
                    "select * from books where id = :id", params, new BookMapper()
            );
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Book> getByName(String name) {
        try {
            Map<String, Object> params = Collections.singletonMap("name", name);
            return namedParameterJdbcOperations.query("select * from books where :name = name", params, new BookMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Book> getAll() {
        try {
            return namedParameterJdbcOperations.query("select * from books", new BookMapper());
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
                    "delete from books where id = :id", params
            );
        }
        catch (EmptyResultDataAccessException e) {
            log.warn("Error in deleteById method", e);
        }
    }

    @Override
    public void updateById(long id, Book newBook) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            params.put("name", newBook.getName());
            params.put("authorId", newBook.getAuthorId());
            params.put("genreId", newBook.getGenreId());
            namedParameterJdbcOperations.update(
                    "update books set name = :name, author_id = :authorId, genre_id = :genreId where id = :id", params
            );
        }
        catch (EmptyResultDataAccessException e) {
            log.warn("Error in updateById method", e);
        }
    }

    private static class BookMapper implements RowMapper<Book> {

        private BookMapper() { }

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            long authorId = resultSet.getLong("author_Id");
            long genreId = resultSet.getLong("genre_Id");
            return new Book(id, name, authorId, genreId);
        }
    }
}
