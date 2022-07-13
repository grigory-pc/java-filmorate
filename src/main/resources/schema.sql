DROP TABLE IF EXISTS users, films, friendship, film_like, film_genre, genre, mpa;

CREATE TABLE IF NOT EXISTS `users`
(
    `id`       long PRIMARY KEY AUTO_INCREMENT,
    `email`    varchar(50) NOT NULL,
    `login`    varchar(50) NOT NULL,
    `name`     varchar(50) NOT NULL,
    `birthday` date NOT NULL
);

CREATE TABLE IF NOT EXISTS `films`
(
    `film_id`      long PRIMARY KEY AUTO_INCREMENT,
    `name`         varchar(50) NOT NULL,
    `rate`         long NOT NULL,
    `mpa_id`       int NOT NULL,
    `description`  varchar(200) NOT NULL,
    `release_date` date NOT NULL,
    `duration`     int NOT NULL
);

CREATE TABLE IF NOT EXISTS `friendship`
(
    `user1_id` long NOT NULL,
    `user2_id` long NOT NULL,
    `status`   varchar(20) NOT NULL,
    PRIMARY KEY (`user1_id`, `user2_id`)
);

CREATE TABLE IF NOT EXISTS `film_like`
(
    `film_id` long NOT NULL,
    `user_id` long NOT NULL,
    PRIMARY KEY (`film_id`, `user_id`)
);

CREATE TABLE IF NOT EXISTS `film_genre`
(
    `film_id`  long NOT NULL,
    `genre_id` int NOT NULL,
    PRIMARY KEY (`film_id`, `genre_id`)
);

CREATE TABLE IF NOT EXISTS `genre`
(
    `genre_id` int PRIMARY KEY NOT NULL,
    `name`     varchar(50)
);

CREATE TABLE IF NOT EXISTS `mpa`
(
    `mpa_id` int PRIMARY KEY NOT NULL,
    `name`   varchar(5)
);


ALTER TABLE `film_genre`
    ADD FOREIGN KEY (`film_id`) REFERENCES `films` (`film_id`) ON DELETE CASCADE ;

ALTER TABLE `films`
    ADD FOREIGN KEY (`mpa_id`) REFERENCES `mpa` (`mpa_id`) ON DELETE CASCADE;

ALTER TABLE `friendship`
    ADD FOREIGN KEY (`user1_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

ALTER TABLE `friendship`
    ADD FOREIGN KEY (`user2_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

ALTER TABLE `film_like`
    ADD FOREIGN KEY (`film_id`) REFERENCES `films` (`film_id`) ON DELETE CASCADE;

ALTER TABLE `film_like`
    ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

ALTER TABLE `film_genre`
    ADD FOREIGN KEY (`genre_id`) REFERENCES `genre` (`genre_id`) ON DELETE CASCADE;

