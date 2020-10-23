package ru.otus.spring.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Data
@EqualsAndHashCode(exclude = "booksRef")
@ToString(exclude = "booksRef")
public class Genre {
    private final long id;
    private final String name;
    private BooksRef booksRef;
}
