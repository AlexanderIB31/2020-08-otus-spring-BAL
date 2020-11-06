package ru.otus.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DAO для работы с жанрами")
@DataJpaTest
@Import(GenreRepositoryJpa.class)
public class GenreRepositoryJpaTest {

    private static final String EXPECTED_GENRE_NAME = "Humor";
    private static final long EXPECTED_GENRE_ID = 1;
    private final int EXPECTED_COUNT_GENRES = 1;

    @Autowired
    private GenreRepository genreRepositoryJpa;

    @DisplayName("должен возвращать кол-во жанров")
    @Test
    public void shouldReturnCountOfGenres() {
        long count = genreRepositoryJpa.count();

        assertThat(count).isEqualTo(EXPECTED_COUNT_GENRES);
    }

    @DisplayName("должен добавлять новый жанр в таблицу")
    @Test
    void shouldInsertGenreWithUniqIdAndNameIsDrama() {
        Genre drama = new Genre();
        drama.setName("Drama");
        genreRepositoryJpa.insert(drama);

        Genre genreInserted = genreRepositoryJpa.getById(drama.getId());

        assertThat(genreInserted).isEqualTo(drama);
        assertThat(genreRepositoryJpa.count()).isEqualTo(EXPECTED_COUNT_GENRES + 1);
    }

    @DisplayName("должен возвращать жанр с Id = 1 и типом Юмор")
    @Test
    void shouldReturnGenreByIdWithNameIsHumor() {
        Genre genreFounded = genreRepositoryJpa.getById(1);

        assertThat(genreFounded.getName()).isEqualTo(EXPECTED_GENRE_NAME);
        assertThat(genreFounded.getId()).isEqualTo(EXPECTED_GENRE_ID);
    }

    @DisplayName("должен возвращать жанр с типом Юмор")
    @Test
    void shouldReturnGenreByNameHumor() {
        Genre genreFounded = genreRepositoryJpa.getByName(EXPECTED_GENRE_NAME);

        assertThat(genreFounded.getName()).isEqualTo(EXPECTED_GENRE_NAME);
        assertThat(genreFounded.getId()).isEqualTo(EXPECTED_GENRE_ID);
    }

    @DisplayName("должен возвращать все жанры")
    @Test
    void shouldReturnCountGenresIs2ThereAreHumorAndDrama() {
        Genre drama = new Genre();
        drama.setName("Drama");
        Genre humor = new Genre();
        humor.setName("Humor");
        genreRepositoryJpa.insert(drama);

        List<Genre> genres = genreRepositoryJpa.getAll();

        assertThat(genres.stream().map(Genre::getName)).containsExactlyInAnyOrder(humor.getName(), drama.getName());
    }

    @DisplayName("должен удалить жанр Юмор по Id")
    @Test
    void shouldDeleteGenreHumorById_ThenCountOfGenresShouldBeEqual0() {
        genreRepositoryJpa.deleteById(EXPECTED_GENRE_ID);

        long count = genreRepositoryJpa.count();

        assertThat(count).isEqualTo(0);
    }

    @DisplayName("должен обновить название жанра с Юмор на Триллер")
    @Test
    void shouldUpdateGenreNameFromHumorToThriller() {
        Genre nGenre = new Genre();
        Genre existsGenre = genreRepositoryJpa.getById(EXPECTED_GENRE_ID);
        nGenre.setId(existsGenre.getId());
        nGenre.setName("Thriller");
        genreRepositoryJpa.update(nGenre);

        Genre updatedGenre = genreRepositoryJpa.getById(EXPECTED_GENRE_ID);

        assertThat(updatedGenre).isEqualTo(nGenre);
    }
}
