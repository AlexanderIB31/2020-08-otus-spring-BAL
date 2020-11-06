DROP TABLE IF EXISTS AUTHORS;
CREATE TABLE AUTHORS(ID BIGINT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR(255) UNIQUE);

DROP TABLE IF EXISTS GENRES;
CREATE TABLE GENRES(ID BIGINT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR(255) UNIQUE);

DROP TABLE IF EXISTS BOOKS;
CREATE TABLE BOOKS(
        ID BIGINT PRIMARY KEY AUTO_INCREMENT,
        NAME VARCHAR(255),
        AUTHOR_ID BIGINT,
        GENRE_ID BIGINT,
        CONSTRAINT FK_BOOK_AUTHOR FOREIGN KEY(AUTHOR_ID) REFERENCES AUTHORS(id) ON DELETE CASCADE,
        CONSTRAINT FK_BOOK_GENRE FOREIGN KEY(GENRE_ID) REFERENCES GENRES(id) ON DELETE CASCADE);

DROP TABLE IF EXISTS COMMENTS;
CREATE TABLE COMMENTS(
        ID BIGINT PRIMARY KEY AUTO_INCREMENT,
        TEXT VARCHAR(4096) UNIQUE,
        BOOK_ID BIGINT,
        CONSTRAINT FK_COMMENT_BOOK FOREIGN KEY(BOOK_ID) REFERENCES BOOKS(id) ON DELETE CASCADE);

CREATE UNIQUE INDEX NAME_AUTHOR
ON AUTHORS (NAME);

CREATE UNIQUE INDEX NAME_GENRE
ON GENRES (NAME);