package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

/**
 * Основной контроллер для работы с фильмами
 */
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    /**
     * Возвращает список всех фильмов
     */
    @GetMapping()
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", filmService.getFilmAll().size());

        return filmService.getFilmAll();
    }

    /**
     * Возвращает фильм по id
     *
     * @param id объекта фильма
     * @return объект фильма
     */
    @GetMapping("/{id}")
    public Film getFilmById(@Valid @PathVariable long id) {
        log.debug("Поиск фильма: {}", filmService.getFilmById(id).getName());

        return filmService.getFilmById(id);
    }

    /**
     * Создаёт объект фильма
     *
     * @return возвращает объект фильма, который был создан
     */
    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        log.trace(String.valueOf(film));

        return filmService.add(film);
    }

    /**
     * Обновляет данные фильма
     *
     * @return возвращает обновленный объект фильма
     */
    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        log.trace(String.valueOf(film));

        return filmService.update(film);
    }

    /**
     * Удаляет фильм
     *
     * @param id объекта фильма
     * @return id объекта фильма
     */
    @DeleteMapping("/{id}")
    public boolean delete(@Valid @PathVariable long id) {
        log.trace(String.valueOf(id));

        return filmService.delete(id);
    }

    /**
     * Добавляет лайк фильму
     *
     * @param id     объекта фильма
     * @param userId объекта пользователя, который добавляет лайк
     * @return boolean результата добавления лайка к фильму
     */
    @PutMapping("/{id}/like/{userId}")
    public boolean addFilmLike(@Valid @PathVariable long id, @PathVariable long userId) {
        log.trace(String.valueOf(id));
        log.trace(String.valueOf(userId));

        return filmService.addLike(id, userId);
    }

    /**
     * Удаляет лайк у фильма
     *
     * @param id     объекта фильма
     * @param userId объекта пользователя, который удаляет лайк
     * @return boolean результата удаления лайка у фильма
     */
    @DeleteMapping("/{id}/like/{userId}")
    public boolean deleteLike(@Valid @PathVariable long id, @PathVariable long userId) {
        log.trace(String.valueOf(id));
        log.trace(String.valueOf(userId));

        return filmService.deleteLike(id, userId);
    }

    /**
     * Возвращает список популярных фильмов по количеству лайков
     *
     * @param count количества возвращаемых популярных фильмов
     * @return список популярных фильмов
     */
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false) Integer count) {
        log.debug("Текущее количество Фильмов: {}", filmService.getFilmAll().size());
        if (count == null) {
            count = 10;
        }
        System.out.println(filmService.getPopularFilms(count));
        return filmService.getPopularFilms(count);
    }
}





