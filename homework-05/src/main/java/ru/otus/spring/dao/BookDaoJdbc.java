package ru.otus.spring.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.AuthorRef;
import ru.otus.spring.domain.GenreRef;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"ConstantConditions", "SqlDialectInspection"})
@Repository
@Slf4j
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final AuthorDao authorDaoJdbc;
    private final GenreDao genreDaoJdbc;

    public BookDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations, AuthorDao authorDaoJdbc, GenreDao genreDaoJdbc)
    {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.authorDaoJdbc = authorDaoJdbc;
        this.genreDaoJdbc = genreDaoJdbc;
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
            params.put("authorId", book.getAuthorRef().getAuthorId());
            params.put("genreId", book.getGenreRef().getGenreId());
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
                    "select id, name, author_id, genre_id from books where id = :id", params, new BookMapper(authorDaoJdbc, genreDaoJdbc)
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
            return namedParameterJdbcOperations.query("select id, name, author_id, genre_id from books where :name = name", params, new BookMapper(authorDaoJdbc, genreDaoJdbc));
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Book> getAll() {
        try {
            return namedParameterJdbcOperations.query("select id, name, author_id, genre_id from books", new BookMapper(authorDaoJdbc, genreDaoJdbc));
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
            params.put("authorId", newBook.getAuthorRef().getAuthorId());
            params.put("genreId", newBook.getGenreRef().getGenreId());
            namedParameterJdbcOperations.update(
                    "update books set name = :name, author_id = :authorId, genre_id = :genreId where id = :id", params
            );
        }
        catch (EmptyResultDataAccessException e) {
            log.warn("Error in updateById method", e);
        }
    }

    @Override
    public List<Book> getByAuthorIds(List<Long> ids) {
        try {
            Map<String, Object> params = Collections.singletonMap("ids", ids);
            return namedParameterJdbcOperations.query("select id, name, author_id, genre_id from books where author_id in (:ids)", params, new BookMapper(authorDaoJdbc, genreDaoJdbc));
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Book> getByGenreIds(List<Long> ids) {
        try {
            Map<String, Object> params = Collections.singletonMap("ids", ids);
            return namedParameterJdbcOperations.query("select id, name, author_id, genre_id from books where genre_id in (:ids)", params, new BookMapper(authorDaoJdbc, genreDaoJdbc));
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private static class BookMapper implements RowMapper<Book> {

        private final AuthorDao authorDaoJdbc;
        private final GenreDao genreDaoJdbc;

        private BookMapper(AuthorDao authorDaoJdbc, GenreDao genreDaoJdbc) {
            this.authorDaoJdbc = authorDaoJdbc;
            this.genreDaoJdbc = genreDaoJdbc;
        }

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            long authorId = resultSet.getLong("author_Id");
            long genreId = resultSet.getLong("genre_Id");
            AuthorRef authorRef = new AuthorRef(authorDaoJdbc::getById, authorId);
            GenreRef genreRef = new GenreRef(genreDaoJdbc::getById, genreId);
            return new Book(id, name, authorRef, genreRef);
        }
    }
}
