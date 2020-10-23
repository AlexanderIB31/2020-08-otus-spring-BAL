package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.*;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DAO для работы с книгами")
@JdbcTest
@Import({BookDaoJdbc.class, AuthorDaoJdbc.class, GenreDaoJdbc.class})
public class BookDaoJdbcTest {

    private static final String EXPECTED_BOOK_NAME = "The Adventures of Tom Sawyer";
    private static final long EXPECTED_BOOK_ID = 1;
    private final int EXPECTED_COUNT_BOOKS = 1;

    @Autowired
    private AuthorDao authorDaoJdbc;

    @Autowired
    private GenreDao genreDaoJdbc;

    @Autowired
    private BookDao bookDaoJdbc;

    @DisplayName("должен возвращать кол-во книг")
    @Test
    public void shouldReturnCountOfAuthors() {
        long count = bookDaoJdbc.count();

        assertThat(count).isEqualTo(EXPECTED_COUNT_BOOKS);
    }

    @DisplayName("должен добавлять новую книгу в таблицу")
    @Test
    void shouldInsertBookWithUniqIdAndNameIsRobinHoodAndAuthorIdIs1AndGenreIdIs1() {
        Author author = new Author(1, "Masha");
        Genre genre = new Genre(1, "Humor");
        Book book = new Book(2, "Robin Hood", new AuthorRef((authId) -> author, author.getId()), new GenreRef((genId) -> genre, genre.getId()));
        bookDaoJdbc.insert(book);

        Book bookInserted = bookDaoJdbc.getById(book.getId());

        assertThat(bookInserted).isEqualTo(book);
        assertThat(bookDaoJdbc.count()).isEqualTo(EXPECTED_COUNT_BOOKS + 1);
    }

    @DisplayName("должен возвращать книгу с Id = 1 и названием The Adventures of Tom Sawyer")
    @Test
    void shouldReturnBookByIdWithNameIsTheAdventuresOfTomSawyer() {
        Book bookFounded = bookDaoJdbc.getById(1);

        assertThat(bookFounded.getName()).isEqualTo(EXPECTED_BOOK_NAME);
        assertThat(bookFounded.getId()).isEqualTo(EXPECTED_BOOK_ID);
    }

    @DisplayName("должен возвращать все книги")
    @Test
    void shouldReturnCountBooksIs2ThereAreTheAdventuresOfTomSawyerAndRobinHood() {
        Author author = new Author(1, "Masha");
        Genre genre = new Genre(1, "Humor");
        Book theAdventuresOfTomSawyer = new Book(1, "The Adventures of Tom Sawyer", new AuthorRef((authId) -> author, author.getId()), new GenreRef((genId) -> genre, genre.getId()));
        Book robinHood = new Book(2, "Robin Hood", new AuthorRef((authId) -> author, author.getId()), new GenreRef((genId) -> genre, genre.getId()));
        bookDaoJdbc.insert(robinHood);

        List<Book> books = bookDaoJdbc.getAll();

        assertThat(books).containsExactlyInAnyOrder(theAdventuresOfTomSawyer, robinHood);
    }

    @DisplayName("должен удалить книгу The Adventures of Tom Sawyer по Id")
    @Test
    void shouldDeleteBookTheAdventuresOfTomSawyerById_ThenCountOfBooksShouldBeEqual0() {
        bookDaoJdbc.deleteById(EXPECTED_BOOK_ID);

        int count = bookDaoJdbc.count();

        assertThat(count).isEqualTo(0);
    }

    @DisplayName("должен удалить книгу, если автор книги удален из таблицы")
    @Test
    void shouldDeleteBookIfAuthorHasBeenDeleted() {
        authorDaoJdbc.deleteById(1);

        int count = bookDaoJdbc.count();

        assertThat(count).isEqualTo(0);
    }

    @DisplayName("должен удалить книгу, если жанр книги удален из таблицы")
    @Test
    void shouldDeleteBookIfGenreHasBeenDeleted() {
        genreDaoJdbc.deleteById(1);

        int count = bookDaoJdbc.count();

        assertThat(count).isEqualTo(0);
    }

    @DisplayName("должен найти книгу по имени")
    @Test
    void shouldReturnOneBookByNameTomSawyer() {
        List<Book> foundedBooks = bookDaoJdbc.getByName(EXPECTED_BOOK_NAME);

        assertThat(foundedBooks.size()).isEqualTo(1);
        assertThat(foundedBooks.get(0).getName()).isEqualTo(EXPECTED_BOOK_NAME);
    }

    @DisplayName("должен обновить название книги")
    @Test
    void shouldUpdateBookNameFromTheAdventuresOfTomSawyerToRobinHood() {
        Author author = new Author(1, "Masha");
        Genre genre = new Genre(1, "Humor");
        Book nBook = new Book(EXPECTED_BOOK_ID, "Robin Hood", new AuthorRef((authId) -> author, author.getId()), new GenreRef((genId) -> genre, genre.getId()));
        bookDaoJdbc.updateById(EXPECTED_BOOK_ID, nBook);

        Book updatedBook = bookDaoJdbc.getById(EXPECTED_BOOK_ID);

        assertThat(updatedBook).isEqualTo(nBook);
    }

    @DisplayName("должен вернуть книгу The Adventures Of Tom Sawyer после вызова метода getByAuthorIds([1])")
    @Test
    void shouldReturnTheAdventuresOfTomSawyerBook_AfterGetByAuthorIdsIs1() {
        List<Book> books = bookDaoJdbc.getByAuthorIds(Collections.singletonList(1L));

        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(1);
        assertThat(books.get(0).getName()).isEqualTo(EXPECTED_BOOK_NAME);
    }

    @DisplayName("должен вернуть книгу The Adventures Of Tom Sawyer после вызова метода getByGenreIds([1])")
    @Test
    void shouldReturnTheAdventuresOfTomSawyerBook_AfterGetByGenreIdsIs1() {
        List<Book> books = bookDaoJdbc.getByGenreIds(Collections.singletonList(1L));

        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(1);
        assertThat(books.get(0).getName()).isEqualTo(EXPECTED_BOOK_NAME);
    }
}