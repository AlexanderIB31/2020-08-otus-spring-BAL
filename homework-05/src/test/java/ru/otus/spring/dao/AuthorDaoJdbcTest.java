package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DAO для работы с авторами")
@JdbcTest
@Import(AuthorDaoJdbc.class)
public class AuthorDaoJdbcTest {

    private static final String EXPECTED_AUTHOR_NAME = "Masha";
    private static final long EXPECTED_AUTHOR_ID = 1;
    private final int EXPECTED_COUNT_AUTHORS = 1;
    @MockBean
    private BookDao bookDaoJdbc;

    @Autowired
    private AuthorDao authorDaoJdbc;

    @DisplayName("должен возвращать кол-во авторов")
    @Test
    public void shouldReturnCountOfAuthors() {
        long count = authorDaoJdbc.count();

        assertThat(count).isEqualTo(EXPECTED_COUNT_AUTHORS);
    }

    @DisplayName("должен добавлять нового автора в таблицу")
    @Test
    void shouldInsertAuthorWithUniqIdAndNameIsAlex() {
        Author author = new Author(2, "Alex");
        authorDaoJdbc.insert(author);

        Author authorInserted = authorDaoJdbc.getById(author.getId());

        assertThat(authorInserted).isEqualTo(author);
        assertThat(authorDaoJdbc.count()).isEqualTo(EXPECTED_COUNT_AUTHORS + 1);
    }

    @DisplayName("должен возвращать автора с именем Маша")
    @Test
    void shouldReturnAuthorByNameMasha() {
        Author authorFounded = authorDaoJdbc.getByName(EXPECTED_AUTHOR_NAME);

        assertThat(authorFounded.getName()).isEqualTo(EXPECTED_AUTHOR_NAME);
        assertThat(authorFounded.getId()).isEqualTo(EXPECTED_AUTHOR_ID);
    }

    @DisplayName("должен возвращать автора с Id = 1 и именем Маша")
    @Test
    void shouldReturnAuthorByIdWithNameIsMasha() {
        Author authorFounded = authorDaoJdbc.getById(1);

        assertThat(authorFounded.getName()).isEqualTo(EXPECTED_AUTHOR_NAME);
        assertThat(authorFounded.getId()).isEqualTo(EXPECTED_AUTHOR_ID);
    }

    @DisplayName("должен возвращать всех авторов")
    @Test
    void shouldReturnCountAuthorsIs2ThereAreMashaAndMisha() {
        Author misha = new Author(2, "Misha");
        Author masha = new Author(1, "Masha");
        authorDaoJdbc.insert(misha);

        List<Author> authors = authorDaoJdbc.getAll();

        assertThat(authors).containsExactlyInAnyOrder(misha, masha);
    }

    @DisplayName("должен удалить автора Маша по Id")
    @Test
    void shouldDeleteAuthorMashaById_ThenCountOfAuthorsShouldBeEqual0() {
        authorDaoJdbc.deleteById(EXPECTED_AUTHOR_ID);

        int count = authorDaoJdbc.count();

        assertThat(count).isEqualTo(0);
    }

    @DisplayName("должен обновить имя автора с Маша на Гриша")
    @Test
    void shouldUpdateAuthorNameFromMashaToGrisha() {
        Author nAuthor = new Author(EXPECTED_AUTHOR_ID, "Grisha");
        authorDaoJdbc.updateById(EXPECTED_AUTHOR_ID, nAuthor);

        Author updatedAuthor = authorDaoJdbc.getById(EXPECTED_AUTHOR_ID);

        assertThat(updatedAuthor).isEqualTo(nAuthor);
    }
}