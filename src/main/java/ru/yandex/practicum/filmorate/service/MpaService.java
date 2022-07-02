package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

/**
 * Класс, ответственный за операции с mpa
 */
@Slf4j
@Service
public class MpaService {
    private final MpaStorage mpaStorage;
    private final Validator validator;

    public MpaService(@Qualifier("mpaDbStorage") MpaStorage mpaStorage, Validator validator) {
        this.mpaStorage = mpaStorage;
        this.validator = validator;
    }

    /**
     * Возвращает список всех mpa
     */
    public List<Mpa> getMpaAll() {
        return mpaStorage.getMpaAll();
    }

    /**
     * Возвращает mpa по id
     */
    public Mpa getMpaById(int mpaId) {
        try {
            if (validator.validationId(mpaId)) {
                throw new NotFoundException("id mpa должен быть больше 0");
            }
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
        return mpaStorage.getMpaById(mpaId);
    }
}