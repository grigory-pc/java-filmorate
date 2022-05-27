package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс, ответственный за операции с фильмами
 */
@Slf4j
@Service
public class FilmService {
    private long id;
    private final FilmStorage filmStorage;
    private final Validator validator;

    @Autowired
    public FilmService( FilmStorage filmStorage, Validator validator) {
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
     * Возвращает фильм по id из списка
     */
    public Film getFilmById(long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    /**
     * Валидирует поля объекта и добавляет объект фильма в список
     */
    public Film add(Film film) {
        try {
            if (validator.validationFilmName(film)) {
                throw new ValidationException("Необходимо указать название фильма");
            } else if (validator.validationFilmDescription(film)) {
                throw new ValidationException("Описание фильма не может превышать 200 символов");
            } else if (validator.validationFilmReleaseDate(film)) {
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
            } else if (validator.validationFilmDuration(film)) {
                throw new ValidationException("Продолжительность фильма не может быть меньше 0");
            } else {
                id = generateId();
                film.setId(id);
                film.setLikes(new HashSet<>());
                return filmStorage.add(film);
            }
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * Валидирует поля объекта и обновляет объект фильма в списке
     */
    public Film update(Film film) {
        List<Film> films = filmStorage.getFilmAll();

        try {
            if (validator.validationFilmName(film)) {
                throw new ValidationException("Необходимо указать название фильма");
            } else if (validator.validationFilmDescription(film)) {
                throw new ValidationException("Описание фильма не может превышать 200 символов");
            } else if (validator.validationFilmReleaseDate(film)) {
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
            } else if (validator.validationFilmDuration(film)) {
                throw new ValidationException("Продолжительность фильма не может быть меньше 0");
            } else {
                for (int i = 0; i < films.size(); i++) {
                    if (films.get(i).getId() == film.getId()) {
                        Film filmExisting = films.get(i);
                        filmExisting.setDescription(film.getDescription());
                        filmExisting.setName(film.getName());
                        filmExisting.setDuration(film.getDuration());
                        filmExisting.setReleaseDate(film.getReleaseDate());

                        return filmStorage.update(i, filmExisting);
                    }
                }
                throw new NotFoundException(String.format("Фильм с id %d не найден",
                        film.getId()));
            }
        } catch (ValidationException | NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * Удаляет объект фильма из списка
     */
    public long delete(long id) {
        List<Film> films = filmStorage.getFilmAll();

        try {
            if (validator.validationId(id)) {
                throw new NotFoundException("id фильма должен быть больше 0");
            } else {
                for (int i = 0; i < films.size(); i++) {
                    if (films.get(i).getId() == id) {
                        return filmStorage.delete(i);
                    }
                }
                throw new NotFoundException(String.format("Фильм с id %d не найден", id));
            }
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * Добавление лайка в коллекцию для определенного фильма
     */
    public long addLike(long userId, long filmId) {
        try {
            if (validator.validationId(userId) || validator.validationId(filmId)) {
                throw new NotFoundException("id пользователя или фильма должен быть больше 0");
            }
            filmStorage.getFilmById(filmId).addLike(userId);
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
        return userId;
    }

    /**
     * Удаление лайка из коллекции для определенного фильма
     */
    public long deleteLike(long userId, long filmId) {
        try {
            if (validator.validationId(userId) || validator.validationId(filmId)) {
                throw new NotFoundException("id пользователя или фильма должен быть больше 0");
            }
            filmStorage.getFilmById(filmId).deleteLike(userId);
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
        return userId;
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

    /**
     * Создание нового id для добавления нового фильма в список
     */
    long generateId() {
        return ++id;
    }
}
