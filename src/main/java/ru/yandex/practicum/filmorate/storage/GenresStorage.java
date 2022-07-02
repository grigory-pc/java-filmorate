package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;

/**
 * Интерфейс для хранения жанров
 */
@Component
public interface GenresStorage {
    List<Genres> getGenreAll();

    Genres getGenreById(int genreId);
}