package ru.otus.spring.shell;

import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.AuthorRef;
import ru.otus.spring.domain.GenreRef;

import java.util.List;

@ShellComponent
public class ApplicationEventCommands {

    private final BookDao bookDaoJdbc;
    private final AuthorDao authorDaoJdbc;
    private final GenreDao genreDaoJdbc;

    public ApplicationEventCommands(BookDao bookDaoJdbc, AuthorDao authorDaoJdbc, GenreDao genreDaoJdbc) {
        this.bookDaoJdbc = bookDaoJdbc;
        this.authorDaoJdbc = authorDaoJdbc;
        this.genreDaoJdbc = genreDaoJdbc;
    }

    @ShellMethod(value = "Print tables", key = {"print"})
    public void print() {
        System.out.println("Table BOOKS:");
        System.out.println("ID | NAME | AUTHOR_NAME | GENRE_NAME");
        bookDaoJdbc.getAll().forEach(b -> {
            Author author = b.getAuthorRef().getAuthor();
            Genre genre = b.getGenreRef().getGenre();
            System.out.println(String.format("%d | %s | %s | %s", b.getId(), b.getName(), author.getName(), genre.getName()));
        });
    }

    @ShellMethod(value = "Create book", key = {"create"})
    public void create(String bookName, String authorName, String genreName) {
        Author authorFounded = authorDaoJdbc.getByName(authorName);
        Genre genreFounded = genreDaoJdbc.getByName(genreName);
        if (authorFounded == null) {
            authorFounded = new Author(authorDaoJdbc.count() + 1, authorName);
            authorDaoJdbc.insert(authorFounded);
        }
        if (genreFounded == null) {
            genreFounded = new Genre(genreDaoJdbc.count() + 1, genreName);
            genreDaoJdbc.insert(genreFounded);
        }

        Author finalAuthorFounded = authorFounded;
        Genre finalGenreFounded = genreFounded;
        Book newBook = new Book(bookDaoJdbc.count() + 1, bookName, new AuthorRef((authId) -> finalAuthorFounded, authorFounded.getId()), new GenreRef((genId) -> finalGenreFounded, genreFounded.getId()));
        bookDaoJdbc.insert(newBook);
    }

    @ShellMethod(value = "Delete book by name", key = {"deleteByName"})
    public void deleteByName(String bookName) {
        List<Book> foundedBooks = bookDaoJdbc.getByName(bookName);
        foundedBooks.forEach(b -> bookDaoJdbc.deleteById(b.getId()));
    }

    @ShellMethod(value = "Update book by name", key = {"updateByName"})
    public void updateByName(String bookName,
                             @ShellOption(value = "--name", defaultValue = StringUtils.EMPTY) String newName,
                             @ShellOption(value = "--author", defaultValue = StringUtils.EMPTY) String newAuthorName,
                             @ShellOption(value = "--genre", defaultValue = StringUtils.EMPTY) String newGenreName) {
        List<Book> foundedBooks = bookDaoJdbc.getByName(bookName);
        foundedBooks.forEach(b -> {
            Author nAuthor = b.getAuthorRef().getAuthor();
            Genre nGenre = b.getGenreRef().getGenre();
            if (!newAuthorName.equals(StringUtils.EMPTY)) {
                authorDaoJdbc.updateById(nAuthor.getId(), new Author(nAuthor.getId(), newAuthorName));
            }
            if (!newGenreName.equals(StringUtils.EMPTY)) {
                genreDaoJdbc.updateById(nGenre.getId(), new Genre(nGenre.getId(), newGenreName));
            }
            String name = newName.equals(StringUtils.EMPTY) ? b.getName() : newName;
            bookDaoJdbc.updateById(b.getId(), new Book(b.getId(), name, new AuthorRef((authId) -> nAuthor, nAuthor.getId()), new GenreRef((genId) -> nGenre, nGenre.getId())));
        });
    }
}
