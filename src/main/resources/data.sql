MERGE INTO mpa
(mpa_id, name)
VALUES (1, 'G'),
    (2, 'PG'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17');

MERGE INTO genre
    (genre_id, name)
    VALUES (1, 'Комедия'),
    (2, 'Драма'),
    (3, 'Мультфильм'),
    (4, 'Ужасы'),
    (5, 'Детектив'),
    (6, 'Фантастика');

