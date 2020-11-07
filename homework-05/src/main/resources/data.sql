begin transaction;
insert into authors (id, `name`) values (1, 'Masha');
insert into genres (id, `name`) values (1, 'Humor');
insert into books (id, `name`,`author_id`, `genre_id`) values (1, 'The Adventures of Tom Sawyer', 1, 1);
commit;
