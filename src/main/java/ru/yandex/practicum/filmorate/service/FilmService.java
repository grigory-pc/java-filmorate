package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

/**
 * Класс, ответственный за операции с фильмами
 */
@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final Validator validator;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, Validator validator) {
        this.filmStorage = filmStorage;
        this.validator = validator;
    }

    /**
     * Возвращает список всех фильмов
     */
    public List<Film> getFilmAll() {
        return filmStorage.getFilmAll();
    }

    /**
     * Возвращает фильм по id
     */
    public Film getFilmById(long filmId) {
        try {
            if (validator.validationId(filmId)) {
                throw new NotFoundException("id пользователя должен быть больше 0");
            }
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
        return filmStorage.getFilmById(filmId);
    }

    /**
     * Валидирует поля объекта и добавляет объект фильма
     */
    public Film add(Film film) {
        validationFieldsFilm(film);

        return filmStorage.add(film);
    }

    /**
     * Валидирует поля объекта и обновляет объект фильма
     */
    public Film update(Film film) {
        try {
            if (validator.validationId(film.getId())) {
                throw new NotFoundException("id пользователя должен быть больше 0");
            }
            validationFieldsFilm(film);

            return filmStorage.update(film);
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * Удаление фильма
     */
    public boolean delete(long id) {
        return filmStorage.delete(getFilmById(id));
    }

    /**
     * Добавление лайка для определенного фильма
     */
    public boolean addLike(long userId, long filmId) {
        try {
            if (validator.validationId(userId) || validator.validationId(filmId)) {
                throw new NotFoundException("id пользователя или фильма должен быть больше 0");
            }
            return filmStorage.addLike(userId, filmId);
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * Удаление лайка для определенного фильма
     */
    public boolean deleteLike(long filmId, long userId) {
        try {
            if (validator.validationId(filmId) || validator.validationId(userId)) {
                throw new NotFoundException("id пользователя или фильма должен быть больше 0");
            }
            return filmStorage.deleteLike(filmId, userId);
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * Формирование списка популярных фильмов на базе количества лайков
     */
    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(count);
    }

    /**
     * Валидация полей объекта
     */
    public void validationFieldsFilm(Film film) {
        try {
            if (validator.validationFilmName(film)) {
                throw new ValidationException("Необходимо указать название фильма");
            } else if (validator.validationFilmDescription(film)) {
                throw new ValidationException("Описание фильма не может превышать 200 символов");
            } else if (validator.validationFilmReleaseDate(film)) {
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
            } else if (validator.validationFilmDuration(film)) {
                throw new ValidationException("Продолжительность фильма не может быть меньше 0");
            }
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }
}