package ru.otus.spring.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.function.Function;

@Data
@RequiredArgsConstructor
public class BooksRef {
    private final Function<List<Long>, List<Book>> funcGetBooksByIds;
    private final List<Long> ids;

    public List<Book> getBooks() {
        return funcGetBooksByIds.apply(ids);
    }
}
