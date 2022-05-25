package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

/**
 * Интерфейс для хранения фильмов
 */
@Component
public interface FilmStorage {
    Film add(Film film);

    List<Film> getFilmAll();

    Film getFilmById(long filmId);
}
