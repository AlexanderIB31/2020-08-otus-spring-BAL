package ru.otus.spring.domain;

import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "BOOKS")
@Data
@EqualsAndHashCode(exclude = "comments")
@ToString(exclude = "comments")
@NamedEntityGraph(name = "book-entity-graph",
        attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("genre")})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Fetch(FetchMode.JOIN)
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Fetch(FetchMode.JOIN)
    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
    @BatchSize(size = 10)
    private List<Comment> comments;
}
