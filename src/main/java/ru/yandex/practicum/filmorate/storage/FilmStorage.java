package ru.yandex.practicum.filmorate.storage;

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

    Film update(int i, Film film);

    long delete(int i);
}
