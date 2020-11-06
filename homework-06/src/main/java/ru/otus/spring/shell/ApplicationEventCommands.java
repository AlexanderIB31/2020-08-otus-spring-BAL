package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.domain.Book;
import ru.otus.spring.service.BookService;

@ShellComponent
@RequiredArgsConstructor
public class ApplicationEventCommands {

    private final BookService bookServiceImpl;

    @ShellMethod(value = "Print tables", key = {"print"})
    public void print() {
        System.out.println(bookServiceImpl.print().toString());
    }

    @ShellMethod(value = "Create book", key = {"create"})
    public void create(String bookName, String authorName, String genreName) {
        Book book = bookServiceImpl.createBook(bookName, authorName, genreName);
        System.out.println(String.format("BookId is %d", book.getId()));
    }

    @ShellMethod(value = "Delete book by name", key = {"deleteByName"})
    public void deleteByName(String bookName) {
        bookServiceImpl.deleteBookByName(bookName);
    }

    @ShellMethod(value = "Update book by name", key = {"updateByName"})
    public void updateByName(String bookName,
                             @ShellOption(value = "--name", defaultValue = StringUtils.EMPTY) String newName,
                             @ShellOption(value = "--author", defaultValue = StringUtils.EMPTY) String newAuthorName,
                             @ShellOption(value = "--genre", defaultValue = StringUtils.EMPTY) String newGenreName) {
        bookServiceImpl.updateBookByName(bookName, newName, newAuthorName, newGenreName);
    }

    @ShellMethod(value = "Added comment for book", key = {"addComment"})
    public void addedComment4Book(String bookName, String comment) {
        Book book = bookServiceImpl.addComment4Book(bookName, comment);
        if (book != null) {
            System.out.println(String.format("Comment: '%s' was append to book (%s)", comment, book.getName()));
        }
        else {
            System.out.println("Book with that name is not found, try again!");
        }
    }
}
