package ru.otus.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring.domain.Author;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DAO для работы с авторами")
@DataJpaTest
@Import(AuthorRepositoryJpa.class)
public class AuthorRepositoryJpaTest {

    private static final String EXPECTED_AUTHOR_NAME = "Masha";
    private static final long EXPECTED_AUTHOR_ID = 1;
    private final int EXPECTED_COUNT_AUTHORS = 1;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private AuthorRepository authorRepositoryJpa;

    @DisplayName("должен возвращать кол-во авторов")
    @Test
    public void shouldReturnCountOfAuthors() {
        long count = authorRepositoryJpa.count();

        assertThat(count).isEqualTo(EXPECTED_COUNT_AUTHORS);
    }

    @DisplayName("должен добавлять нового автора в таблицу")
    @Test
    void shouldInsertAuthorWithUniqIdAndNameIsAlex() {
        Author author = new Author();
        author.setName("Alex");
        authorRepositoryJpa.insert(author);

        Author authorInserted = authorRepositoryJpa.getById(author.getId());

        assertThat(authorInserted).isEqualTo(author);
        assertThat(authorRepositoryJpa.count()).isEqualTo(EXPECTED_COUNT_AUTHORS + 1);
    }

    @DisplayName("должен возвращать автора с именем Маша")
    @Test
    void shouldReturnAuthorByNameMasha() {
        Author authorFounded = authorRepositoryJpa.getByName(EXPECTED_AUTHOR_NAME);

        assertThat(authorFounded.getName()).isEqualTo(EXPECTED_AUTHOR_NAME);
        assertThat(authorFounded.getId()).isEqualTo(EXPECTED_AUTHOR_ID);
    }

    @DisplayName("должен возвращать автора с Id = 1 и именем Маша")
    @Test
    void shouldReturnAuthorByIdWithNameIsMasha() {
        Author authorFounded = authorRepositoryJpa.getById(1);

        assertThat(authorFounded.getName()).isEqualTo(EXPECTED_AUTHOR_NAME);
        assertThat(authorFounded.getId()).isEqualTo(EXPECTED_AUTHOR_ID);
    }

    @DisplayName("должен возвращать всех авторов")
    @Test
    void shouldReturnCountAuthorsIs2ThereAreMashaAndMisha() {
        Author misha = new Author();
        misha.setName("Misha");
        Author masha = new Author();
        masha.setName("Masha");
        masha.setId(1);
        authorRepositoryJpa.insert(misha);

        List<Author> authors = authorRepositoryJpa.getAll();

        assertThat(authors).containsExactlyInAnyOrder(misha, masha);
    }

    @DisplayName("должен удалить автора Маша по Id")
    @Test
    void shouldDeleteAuthorMashaById_ThenCountOfAuthorsShouldBeEqual0() {
        authorRepositoryJpa.deleteById(EXPECTED_AUTHOR_ID);

        long count = authorRepositoryJpa.count();

        assertThat(count).isEqualTo(0);
    }

    @DisplayName("должен обновить имя автора с Маша на Гриша")
    @Test
    void shouldUpdateAuthorNameFromMashaToGrisha() {
        Author nAuthor = new Author();
        nAuthor.setId(EXPECTED_AUTHOR_ID);
        nAuthor.setName("Grisha");
        authorRepositoryJpa.update(nAuthor);

        Author updatedAuthor = authorRepositoryJpa.getById(nAuthor.getId());

        assertThat(updatedAuthor).isEqualTo(nAuthor);
    }
}