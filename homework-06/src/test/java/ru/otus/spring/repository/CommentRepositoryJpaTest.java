package ru.otus.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DAO для работы с комментариями")
@DataJpaTest
@Import(CommentRepositoryJpa.class)
public class CommentRepositoryJpaTest {

    private static final String EXPECTED_COMMENT_TEXT = "Default comment";
    private static final long EXPECTED_COMMENT_ID = 1;

    @Autowired
    private CommentRepository commentRepositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен возвращать кол-во комментариев")
    @Test
    public void shouldReturnCountOfComments() {
        long count = commentRepositoryJpa.count();

        assertThat(count).isEqualTo(1);
    }

    @DisplayName("должен добавлять новый комментарий к книге The Adventures of Tom Sawyer")
    @Test
    void shouldAddedCommentGoodBookToTheAdventuresOfTomSawyer() {
        Comment comment = new Comment();
        comment.setText("GoodBook");
        Book theAdventuresOfTomSawyer = em.find(Book.class, 1L);
        comment.setBook(theAdventuresOfTomSawyer);

        commentRepositoryJpa.insert(comment);

        assertThat(theAdventuresOfTomSawyer.getComments()).isNotNull().size().isEqualTo(2);
        assertThat(theAdventuresOfTomSawyer.getComments().stream().map(Comment::getText)).contains(comment.getText());
    }

    @DisplayName("должен возвращать комментарий с Id = 1 и текстом Default comment")
    @Test
    void shouldReturnCommentById() {
        Comment comment = commentRepositoryJpa.getById(EXPECTED_COMMENT_ID);

        assertThat(comment.getText()).isEqualTo(EXPECTED_COMMENT_TEXT);
        assertThat(comment.getId()).isEqualTo(EXPECTED_COMMENT_ID);
    }

    @DisplayName("должен удалить комментарий, но книга должна остаться в живых")
    @Test
    void shouldSurviveBookAfterDeleteComment() {
        Book theAdventuresOfTomSawyer = em.find(Book.class, 1L);

        commentRepositoryJpa.deleteById(theAdventuresOfTomSawyer.getComments().get(0).getId());
        em.clear();
        Book surviveBook = em.find(Book.class, 1L);
        Comment comment = em.find(Comment.class, EXPECTED_COMMENT_ID);

        assertThat(surviveBook).isNotNull();
        assertThat(comment).isNull();
    }
}
