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
    public long delete(@Valid @PathVariable long id) {
        log.trace(String.valueOf(id));

        filmService.delete(id);

        log.info("Удалён фильм: {}", filmService.getFilmById(id).getName());

        return id;
    }

    /**
     * Добавляет лайк фильму
     *
     * @param id     объекта фильма
     * @param userId объекта пользователя, который добавляет лайк
     * @return userId объекта пользователя
     */
    @PutMapping("/{id}/like/{userId}")
    public long addFilmLike(@Valid @PathVariable long id, @PathVariable long userId) {
        log.trace(String.valueOf(id));
        log.trace(String.valueOf(userId));

        filmService.addLike(id, userId);

        log.info("Добавлен лайк фильму: {}", filmService.getFilmById(id).getName());

        return userId;
    }

    /**
     * Удаляет лайк у фильма
     *
     * @param id     объекта фильма
     * @param userId объекта пользователя, который удаляет лайк
     * @return userId объекта пользователя
     */
    @DeleteMapping("/{id}/like/{userId}")
    public long deleteLike(@Valid @PathVariable long id, @PathVariable long userId) {
        log.trace(String.valueOf(id));
        log.trace(String.valueOf(userId));

        filmService.deleteLike(id, userId);

        log.info("Удалён лайк для фильма: {}", filmService.getFilmById(id).getName());

        return userId;
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

        return filmService.getPopularFilms(count);
    }
}





