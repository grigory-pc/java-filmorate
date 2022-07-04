package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import javax.validation.Valid;
import java.util.List;

/**
 * Основной контроллер для работы с mpa
 */
@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    /**
     * Возвращает список всех mpa
     */
    @GetMapping()
    public List<Mpa> findAll() {
        log.debug("Текущее количество пользователей: {}", mpaService.getMpaAll().size());

        return mpaService.getMpaAll();
    }

    /**
     * Возвращает mpa по id
     *
     * @param id объекта mpa
     * @return объект mpa
     */
    @GetMapping("/{id}")
    public Mpa getUserById(@Valid @PathVariable int id) {
        log.debug("Поиск mpa: {}", mpaService.getMpaById(id).getName());

        return mpaService.getMpaById(id);
    }
}