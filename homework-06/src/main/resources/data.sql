begin transaction;
insert into authors (`name`) values ('Masha');
insert into genres (`name`) values ('Humor');
insert into books (`name`,`author_id`, `genre_id`) values ('The Adventures of Tom Sawyer', 1, 1);
insert into comments (`text`, `book_id`) values ('Default comment', 1);
commit;
