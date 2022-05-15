package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    public final List<Film> films = new ArrayList<>();

    @GetMapping()
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());

        return films;
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        log.trace(String.valueOf(film));

        try {
            if (validationName(film)) {
                throw new ValidationException("Необходимо указать название фильма");
            } else if (validationDescription(film)) {
                throw new ValidationException("Описание фильма не может превышать 200 символов");
            } else if (validationReleaseDate(film)) {
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
            } else if (validationDuration(film)) {
                throw new ValidationException("Продолжительность фильма не может быть меньше 0");
            } else {
                films.add(film);
            }
        } catch (ValidationException e) {
            log.warn(e.getMessage());
        }
        log.info("Фильм " + film.getName() + " добавлен");

        return film;
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        log.trace(String.valueOf(film));

        try {
            if (validationName(film)) {
                throw new ValidationException("Необходимо указать название фильма");
            } else if (validationDescription(film)) {
                throw new ValidationException("Описание фильма не может превышать 200 символов");
            } else if (validationReleaseDate(film)) {
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
            } else if (validationDuration(film)) {
                throw new ValidationException("Продолжительность фильма не может быть меньше 0");
            } else {
                films.add(film);
            }
        } catch (ValidationException e) {
            log.warn(e.getMessage());
        }
        log.info("Информация о фильме обновлена");

        return film;
    }

    private boolean validationName(Film film) {
        return film.getName().isEmpty();
    }

    private boolean validationDescription(Film film) {
        return film.getDescription().isEmpty() || film.getDescription().length() > 200;
    }

    private boolean validationReleaseDate(Film film) {
        return film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28));
    }

    private boolean validationDuration(Film film) {
        return film.getDuration().isNegative();
    }
}



