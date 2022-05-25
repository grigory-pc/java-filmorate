package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.Validator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс, ответственный за операции с фильмами
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final Validator validator;
    private Set<Long> likes;

    /**
     * Добавление лайка в коллекцию для определенного фильма
     */
    public long addLike(long userId, long filmId) {
        try {
            if (validator.validationId(userId) || validator.validationId(filmId)) {
                throw new NotFoundException("id пользователя или фильма должен быть больше 0");
            }
            likes = filmStorage.getFilmById(filmId).getLikes();
            if (!likes.contains(userId)) {
                likes.add(userId);
                System.out.println("Добавлен лайк фильму" + filmStorage.getFilmById(filmId).getName());
            } else {
                System.out.println("Вы уже поставили лайк этому фильму");
            }
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }

        return filmId;
    }

    /**
     * Удаление лайка из коллекции для определенного фильма
     */
    public long deleteLike(long userId, long filmId) {
        likes = filmStorage.getFilmById(filmId).getLikes();
        try {
            if (validator.validationId(userId) || validator.validationId(filmId)) {
                throw new NotFoundException("id пользователя или фильма должен быть больше 0");
            }
            if (likes.contains(userId)) {
                likes.remove(userId);
                System.out.println("Убран лайк фильму" + filmStorage.getFilmById(filmId).getName());
            } else {
                System.out.println("Вы не лайкали фильм" + filmStorage.getFilmById(filmId).getName());
            }
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }

        return filmId;
    }

    /**
     * Формирование списка популярных фильмов на базе количества лайков
     */
    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getFilmAll().stream().sorted((p0, p1) -> {
            int comp = Integer.compare(p0.getLikes().size(), p1.getLikes().size());
            return comp;
        }).limit(count).collect(Collectors.toList());
    }
}
