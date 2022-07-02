package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.storage.GenresStorage;

import java.util.List;

/**
 * Класс, ответственный за операции с жанрами
 */
@Slf4j
@Service
public class GenresService {
    private final GenresStorage genresStorage;
    private final Validator validator;

    public GenresService(@Qualifier("genresDbStorage") GenresStorage genresStorage, Validator validator) {
        this.genresStorage = genresStorage;
        this.validator = validator;
    }

    /**
     * Возвращает список всех жанров
     */
    public List<Genres> getGenreAll() {
        return genresStorage.getGenreAll();
    }

    /**
     * Возвращает жанр по ID
     */
    public Genres getGenreById(int genreId) {
        try {
            if (validator.validationId(genreId)) {
                throw new NotFoundException("id жанра должен быть больше 0");
            }
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
        return genresStorage.getGenreById(genreId);
    }
}