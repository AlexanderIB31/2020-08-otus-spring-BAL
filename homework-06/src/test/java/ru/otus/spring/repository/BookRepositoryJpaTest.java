package ru.otus.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DAO для работы с книгами")
@DataJpaTest
@Import(BookRepositoryJpa.class)
public class BookRepositoryJpaTest {

    private static final String EXPECTED_BOOK_NAME = "The Adventures of Tom Sawyer";
    private static final long EXPECTED_BOOK_ID = 1;
    private final int EXPECTED_COUNT_BOOKS = 1;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookRepository bookRepositoryJpa;

    @DisplayName("должен возвращать кол-во книг")
    @Test
    public void shouldReturnCountOfAuthors() {
        long count = bookRepositoryJpa.count();

        assertThat(count).isEqualTo(EXPECTED_COUNT_BOOKS);
    }

    @DisplayName("должен добавлять новую книгу в таблицу")
    @Test
    void shouldInsertBookWithUniqIdAndNameIsRobinHoodAndAuthorIdIs1AndGenreIdIs1() {
        Book book = new Book();
        Author author = em.find(Author.class, 1L);
        Genre genre = em.find(Genre.class, 1L);
        book.setName("Robin Hood");
        book.setGenre(genre);
        book.setAuthor(author);

        bookRepositoryJpa.insert(book);
        Book bookInserted = bookRepositoryJpa.getById(book.getId());

        assertThat(bookInserted).isEqualTo(book);
        assertThat(bookRepositoryJpa.count()).isEqualTo(EXPECTED_COUNT_BOOKS + 1);
    }

    @DisplayName("должен возвращать книгу с Id = 1 и названием The Adventures of Tom Sawyer")
    @Test
    void shouldReturnBookByIdWithNameIsTheAdventuresOfTomSawyer() {
        Book bookFounded = bookRepositoryJpa.getById(1);

        assertThat(bookFounded.getName()).isEqualTo(EXPECTED_BOOK_NAME);
        assertThat(bookFounded.getId()).isEqualTo(EXPECTED_BOOK_ID);
    }

    @DisplayName("должен возвращать все книги")
    @Test
    void shouldReturnCountBooksIs2ThereAreTheAdventuresOfTomSawyerAndRobinHood() {
        Book robinHood = new Book();
        Book theAdventuresOfTomSawyer = em.find(Book.class, EXPECTED_BOOK_ID);
        robinHood.setName("Robin Hood");
        robinHood.setAuthor(theAdventuresOfTomSawyer.getAuthor());
        robinHood.setGenre(theAdventuresOfTomSawyer.getGenre());

        bookRepositoryJpa.insert(robinHood);
        List<Book> books = bookRepositoryJpa.getAll();

        assertThat(books.stream().map(Book::getName)).containsExactlyInAnyOrder(theAdventuresOfTomSawyer.getName(), robinHood.getName());
    }

    @DisplayName("должен удалить книгу The Adventures of Tom Sawyer по Id")
    @Test
    void shouldDeleteBookTheAdventuresOfTomSawyerById_ThenCountOfBooksShouldBeEqual0() {
        bookRepositoryJpa.deleteById(EXPECTED_BOOK_ID);

        long count = bookRepositoryJpa.count();

        assertThat(count).isEqualTo(0);
    }

    @DisplayName("должен удалить книгу, если автор книги удален из таблицы")
    @Test
    void shouldDeleteBookIfAuthorHasBeenDeleted() {
        Author author = em.find(Author.class, 1L);
        em.remove(author);

        long count = bookRepositoryJpa.count();

        assertThat(count).isEqualTo(0);
    }

    @DisplayName("должен удалить книгу, если жанр книги удален из таблицы")
    @Test
    void shouldDeleteBookIfGenreHasBeenDeleted() {
        Genre genre = em.find(Genre.class, 1L);
        em.remove(genre);

        long count = bookRepositoryJpa.count();

        assertThat(count).isEqualTo(0);
    }

    @DisplayName("должен найти книгу по имени")
    @Test
    void shouldReturnOneBookByNameTomSawyer() {
        List<Book> foundedBooks = bookRepositoryJpa.getByName(EXPECTED_BOOK_NAME);

        assertThat(foundedBooks.size()).isEqualTo(1);
        assertThat(foundedBooks.get(0).getName()).isEqualTo(EXPECTED_BOOK_NAME);
    }

    @DisplayName("должен обновить название книги")
    @Test
    void shouldUpdateBookNameFromTheAdventuresOfTomSawyerToRobinHood() {
        Book existsBook = em.find(Book.class, EXPECTED_BOOK_ID);
        Book nBook = new Book();
        nBook.setId(existsBook.getId());
        nBook.setName("Robin Hood");
        nBook.setGenre(existsBook.getGenre());
        nBook.setAuthor(existsBook.getAuthor());
        bookRepositoryJpa.update(nBook);

        Book updatedBook = bookRepositoryJpa.getById(EXPECTED_BOOK_ID);

        assertThat(updatedBook).isEqualTo(nBook);
    }

/*    @DisplayName("должен вернуть книгу The Adventures Of Tom Sawyer после вызова метода getByAuthorIds([1])")
    @Test
    void shouldReturnTheAdventuresOfTomSawyerBook_AfterGetByAuthorIdsIs1() {
        List<Book> books = bookRepositoryJpa.getByAuthorIds(Collections.singletonList(1L));

        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(1);
        assertThat(books.get(0).getName()).isEqualTo(EXPECTED_BOOK_NAME);
    }

    @DisplayName("должен вернуть книгу The Adventures Of Tom Sawyer после вызова метода getByGenreIds([1])")
    @Test
    void shouldReturnTheAdventuresOfTomSawyerBook_AfterGetByGenreIdsIs1() {
        List<Book> books = bookRepositoryJpa.getByGenreIds(Collections.singletonList(1L));

        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(1);
        assertThat(books.get(0).getName()).isEqualTo(EXPECTED_BOOK_NAME);
    }*/
}
