package ru.otus.spring.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.otus.spring.domain.Author;

import java.util.function.Function;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "funcGetAuthor")
@ToString(exclude = "funcGetAuthor")
public class AuthorRef {
    private final Function<Long, Author> funcGetAuthor;
    private final long authorId;

    public Author getAuthor() {
        return funcGetAuthor.apply(authorId);
    }
}
