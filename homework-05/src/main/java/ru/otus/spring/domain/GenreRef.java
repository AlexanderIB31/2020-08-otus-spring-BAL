package ru.otus.spring.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.otus.spring.domain.Genre;

import java.util.function.Function;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "funcGetGenre")
@ToString(exclude = "funcGetGenre")
public class GenreRef {
    private final Function<Long, Genre> funcGetGenre;
    private final long genreId;

    public Genre getGenre() {
        return funcGetGenre.apply(genreId);
    }
}
