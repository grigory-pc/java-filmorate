package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.service.GenresService;

import javax.validation.Valid;
import java.util.List;

/**
 * Основной контроллер для работы с жанрами
 */
@Slf4j
@RestController
@RequestMapping("/genres")
public class GenresController {
    private final GenresService genresService;

    @Autowired
    public GenresController(GenresService genresService) {
        this.genresService = genresService;
    }

    /**
     * Возвращает список всех жанров
     */
    @GetMapping()
    public List<Genres> findAll() {
        log.debug("Текущее количество пользователей: {}", genresService.getGenreAll().size());

        return genresService.getGenreAll();
    }

    /**
     * Возвращает жанр по id
     *
     * @param id объекта genre
     * @return объект genre
     */
    @GetMapping("/{id}")
    public Genres getUserById(@Valid @PathVariable int id) {
        log.debug("Поиск genre: {}", genresService.getGenreById(id).getName());

        return genresService.getGenreById(id);
    }

}
