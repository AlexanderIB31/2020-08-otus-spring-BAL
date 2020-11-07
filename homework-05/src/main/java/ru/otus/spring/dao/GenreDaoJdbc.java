package ru.otus.spring.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.BooksRef;
import ru.otus.spring.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings({"ConstantConditions", "SqlDialectInspection"})
@Repository
@Slf4j
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final BookDao bookDaoJdbc;

    public GenreDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations, @Lazy BookDao bookDaoJdbc)
    {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.bookDaoJdbc = bookDaoJdbc;
    }

    @Override
    public int count() {
        return namedParameterJdbcOperations.queryForObject("select count(*) from genres", new HashMap<>(), Integer.class);
    }

    @Override
    public void insert(Genre genre) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", genre.getId());
            params.put("name", genre.getName());
            namedParameterJdbcOperations.update(
                    "insert into genres (id, `name`) values (:id, :name)", params
            );
        }
        catch (EmptyResultDataAccessException e) {
            log.warn("Error in insert method", e);
        }
    }

    @Override
    public Genre getById(long id) {
        try {
            Map<String, Object> params = Collections.singletonMap("id", id);
            Genre genre = namedParameterJdbcOperations.queryForObject(
                    "select id, name from genres where id = :id", params, new GenreMapper()
            );
            BooksRef booksRef = new BooksRef(bookDaoJdbc::getByAuthorIds, Collections.singletonList(genre.getId()));
            genre.setBooksRef(booksRef);
            return genre;
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Genre getByName(String name) {
        try {
            Map<String, Object> params = Collections.singletonMap("name", name);
            Genre genre = namedParameterJdbcOperations.queryForObject(
                    "select id, name from genres where name = :name", params, new GenreMapper()
            );
            BooksRef booksRef = new BooksRef(bookDaoJdbc::getByAuthorIds, Collections.singletonList(genre.getId()));
            genre.setBooksRef(booksRef);
            return genre;
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Genre> getAll() {
        try {
            List<Genre> genres = namedParameterJdbcOperations.query("select id, name from genres", new GenreMapper());
            BooksRef booksRef = new BooksRef(bookDaoJdbc::getByAuthorIds, genres.stream().map(Genre::getId).collect(Collectors.toList()));
            genres.forEach(g -> g.setBooksRef(booksRef));
            return genres;
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
                    "delete from genres where id = :id", params
            );
        }
        catch (EmptyResultDataAccessException e) {
            log.warn("Error in deleteById method", e);
        }
    }

    @Override
    public void updateById(long id, Genre newGenre) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            params.put("name", newGenre.getName());
            namedParameterJdbcOperations.update(
                    "update genres set name = :name where id = :id", params
            );
        }
        catch (EmptyResultDataAccessException e) {
            log.warn("Error in updateById method", e);
        }
    }

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            return new Genre(id, name);
        }
    }
}
