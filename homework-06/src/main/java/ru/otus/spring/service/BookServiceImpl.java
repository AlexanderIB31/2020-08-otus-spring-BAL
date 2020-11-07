package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepositoryJpa;
    private final GenreRepository genreRepositoryJpa;
    private final BookRepository bookRepositoryJpa;
    private final CommentRepository commentRepositoryJpa;

    @Transactional
    @Override
    public Book createBook(String bookName, String authorName, String genreName) {
        Author authorFounded = authorRepositoryJpa.getByName(authorName);
        Genre genreFounded = genreRepositoryJpa.getByName(genreName);
        if (authorFounded == null) {
            authorFounded = new Author();
            authorFounded.setName(authorName);
            authorRepositoryJpa.insert(authorFounded);
        }
        if (genreFounded == null) {
            genreFounded = new Genre();
            genreFounded.setName(genreName);
            genreRepositoryJpa.insert(genreFounded);
        }

        Book newBook = new Book();
        newBook.setName(bookName);
        newBook.setAuthor(authorFounded);
        newBook.setGenre(genreFounded);
        bookRepositoryJpa.insert(newBook);

        return newBook;
    }

    @Override
    @Transactional
    public List<Book> deleteBookByName(String bookName) {
        List<Book> foundedBooks = bookRepositoryJpa.getByName(bookName);
        foundedBooks.forEach(b -> bookRepositoryJpa.deleteById(b.getId()));
        return foundedBooks;
    }

    @Override
    @Transactional
    public void updateBookByName(String bookName, String newName, String newAuthorName, String newGenreName) {
        List<Book> foundedBooks = bookRepositoryJpa.getByName(bookName);
        foundedBooks.forEach(b -> {
            Author nAuthor = b.getAuthor();
            Genre nGenre = b.getGenre();
            if (!newAuthorName.equals(StringUtils.EMPTY)) {
                nAuthor.setName(newAuthorName);
            }
            if (!newGenreName.equals(StringUtils.EMPTY)) {
                nGenre.setName(newGenreName);
            }
            String name = newName.equals(StringUtils.EMPTY) ? b.getName() : newName;
            b.setName(name);
        });
    }

    @Override
    @Transactional
    public Book addComment4Book(String bookName, String commentStr) {
        Comment comment = new Comment();
        comment.setText(commentStr);
        List<Book> books = bookRepositoryJpa.getByName(bookName);
        if (books == null || books.isEmpty()) {
            return null;
        }

        comment.setBook(books.get(0));
        commentRepositoryJpa.insert(comment);
        return books.get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public StringBuilder print() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Table BOOKS:");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("ID | NAME | AUTHOR_NAME | GENRE_NAME | COMMENT");
        stringBuilder.append(System.getProperty("line.separator"));
        bookRepositoryJpa.getAll().forEach(b -> {
            Author author = b.getAuthor();
            Genre genre = b.getGenre();
            List<Comment> comments = b.getComments();
            AtomicLong index = new AtomicLong();
            stringBuilder.append(String.format("%d | %s | %s | %s | %s",
                    b.getId(),
                    b.getName(),
                    author.getName(),
                    genre.getName(),
                    comments.stream()
                            .map(c -> String.format("%d) %s", index.getAndIncrement(), c.getText()))
                            .collect(Collectors.joining(" "))));
            stringBuilder.append(System.getProperty("line.separator"));
        });

        return stringBuilder;
    }
}
