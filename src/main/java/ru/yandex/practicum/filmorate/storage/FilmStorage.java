package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

/**
 * Интерфейс для хранения фильмов
 */
@Component
public interface FilmStorage {
    List<Film> getFilmAll();

    Film getFilmById(long filmId);

    Film add(Film film);

    Film update(Film film);

    boolean delete(Film film);

    List<Film> getPopularFilms(int count);

    boolean addLike(long userId, long filmId);

    boolean deleteLike(long userId, long filmId);
}