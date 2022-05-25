package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Реализация интерфейса для хранения фильмов
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    protected long id;
    private final Validator validator;

    private final List<Film> films = new ArrayList<>();

    /**
     * Добавление фильма в список
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
                films.add(film);
            }
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
        return film;
    }

    /**
     * Обновление фильма в списке
     */
    public Film update(Film film) {
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

                        films.set(i, filmExisting);
                        return filmExisting;
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
     * Получение списка фильмов
     */
    public List<Film> getFilmAll() {
        return films;
    }

    /**
     * Получение фильма из списка по ID
     */
    public Film getFilmById(long filmId) {
        return films.stream()
                .filter(p -> p.getId() == (filmId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с id %d не найден",
                        filmId)));
    }

    /**
     * Создание нового ID для добавления нового фильма в писок
     */
    long generateId() {
        return ++id;
    }
}
