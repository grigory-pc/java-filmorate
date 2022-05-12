package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.naming.InvalidNameException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    public final HashMap<String, Film> films = new HashMap<>();

    @GetMapping()
    public HashMap<String, Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());

        return films;
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        log.trace(String.valueOf(film));

        try {
            if (film.getName().isEmpty()) {
                throw new InvalidNameException("Необходимо указать название фильма");
            } else if (!films.isEmpty() && films.containsKey(film.getName())) {
                throw new FilmAlreadyExistException("Фильм с таким названием уже существует");
            } else if (film.getDescription().isEmpty() || film.getDescription().length() > 200) {
                throw new HighLengthDescriptionException("Описание фильма не может превышать 200 символов");
            } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new OldDateFilmException("Дата релиза не может быть раньше 28 декабря 1895 года");
            } else if (film.getDuration().isNegative()) {
                throw new NegativeDurationException("Продолжительность фильма не может быть меньше 0");
            } else {
                films.put(film.getName(), film);
            }
        } catch (InvalidNameException | OldDateFilmException | NegativeDurationException |
                 HighLengthDescriptionException | FilmAlreadyExistException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        log.info("Фильм " + film.getName() + " добавлен");

        return film;
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        log.trace(String.valueOf(film));

        try {
            if (film.getName().isEmpty()) {
                throw new InvalidNameException("Необходимо указать название фильма");
            } else if (!film.getDescription().isEmpty() && film.getDescription().length() > 200) {
                throw new HighLengthDescriptionException("Описание фильма не может превышать 200 символов");
            } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new OldDateFilmException("Дата релиза не может быть раньше 28 декабря 1895 года");
            } else if (film.getDuration().isNegative()) {
                throw new NegativeDurationException("Продолжительность фильма должна быть положительной");
            } else {
                films.put(film.getName(), film);
            }
        } catch (InvalidNameException | OldDateFilmException | HighLengthDescriptionException |
                 NegativeDurationException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
        log.info("Информация о фильме обновлена");

        return film;
    }

    @ExceptionHandler
    void handleIllegalArgumentException(FilmAlreadyExistException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
