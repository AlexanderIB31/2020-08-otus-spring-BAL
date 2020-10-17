package ru.otus.spring.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Data
//@ToString(exclude = "bookIds")
//@EqualsAndHashCode(exclude = "bookIds")
public class Author {
    private final long id;
    private final String name;
    private List<Book> books = new ArrayList<>();
}
