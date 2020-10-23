package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DAO для работы с жанрами")
@JdbcTest
@Import(GenreDaoJdbc.class)
public class GenreDaoJdbcTest {

    private static final String EXPECTED_GENRE_NAME = "Humor";
    private static final long EXPECTED_GENRE_ID = 1;
    private final int EXPECTED_COUNT_GENRES = 1;
    @MockBean
    private BookDao bookDaoJdbc;

    @Autowired
    private GenreDao genreDaoJdbc;

    @DisplayName("должен возвращать кол-во жанров")
    @Test
    public void shouldReturnCountOfGenres() {
        long count = genreDaoJdbc.count();

        assertThat(count).isEqualTo(EXPECTED_COUNT_GENRES);
    }

    @DisplayName("должен добавлять новый жанр в таблицу")
    @Test
    void shouldInsertGenreWithUniqIdAndNameIsDrama() {
        Genre genre = new Genre(2, "Drama");
        genreDaoJdbc.insert(genre);

        Genre genreInserted = genreDaoJdbc.getById(genre.getId());

        assertThat(genreInserted).isEqualTo(genre);
        assertThat(genreDaoJdbc.count()).isEqualTo(EXPECTED_COUNT_GENRES + 1);
    }

    @DisplayName("должен возвращать жанр с Id = 1 и типом Юмор")
    @Test
    void shouldReturnGenreByIdWithNameIsHumor() {
        Genre genreFounded = genreDaoJdbc.getById(1);

        assertThat(genreFounded.getName()).isEqualTo(EXPECTED_GENRE_NAME);
        assertThat(genreFounded.getId()).isEqualTo(EXPECTED_GENRE_ID);
    }

    @DisplayName("должен возвращать жанр с типом Юмор")
    @Test
    void shouldReturnGenreByNameHumor() {
        Genre genreFounded = genreDaoJdbc.getByName(EXPECTED_GENRE_NAME);

        assertThat(genreFounded.getName()).isEqualTo(EXPECTED_GENRE_NAME);
        assertThat(genreFounded.getId()).isEqualTo(EXPECTED_GENRE_ID);
    }

    @DisplayName("должен возвращать все жанры")
    @Test
    void shouldReturnCountGenresIs2ThereAreHumorAndDrama() {
        Genre drama = new Genre(2, "Drama");
        Genre humor = new Genre(1, "Humor");
        genreDaoJdbc.insert(drama);

        List<Genre> genres = genreDaoJdbc.getAll();

        assertThat(genres).containsExactlyInAnyOrder(humor, drama);
    }

    @DisplayName("должен удалить жанр Юмор по Id")
    @Test
    void shouldDeleteGenreHumorById_ThenCountOfGenresShouldBeEqual0() {
        genreDaoJdbc.deleteById(EXPECTED_GENRE_ID);

        int count = genreDaoJdbc.count();

        assertThat(count).isEqualTo(0);
    }

    @DisplayName("должен обновить название жанра с Юмор на Триллер")
    @Test
    void shouldUpdateGenreNameFromHumorToThriller() {
        Genre nGenre = new Genre(EXPECTED_GENRE_ID, "Thriller");
        genreDaoJdbc.updateById(EXPECTED_GENRE_ID, nGenre);

        Genre updatedGenre = genreDaoJdbc.getById(EXPECTED_GENRE_ID);

        assertThat(updatedGenre).isEqualTo(nGenre);
    }
}