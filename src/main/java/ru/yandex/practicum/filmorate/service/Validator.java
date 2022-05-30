package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

/**
 * Класс, ответственный за валидацию параметров пользователей и фильмов
 */
@Component
public class Validator {

    /**
     * Проверка: электронная почта не может быть пустой и должна содержать символ @
     */
    protected boolean validationUserEmail(User user) {
        return user.getEmail().isEmpty() || !user.getEmail().contains("@");
    }

    /**
     * Проверка: логин не может быть пустым и содержать пробелы
     */
    protected boolean validationUserLogin(User user) {
        return user.getLogin().isEmpty() || user.getLogin().contains(" ");
    }

    /**
     * Проверка: дата рождения не может быть в будущем
     */
    protected boolean validationUserBirthday(User user) {
        return user.getBirthday().isAfter(LocalDate.now());
    }

    /**
     * Проверка: имя для отображения может быть пустым
     */
    protected boolean validationUserName(User user) {
        return user.getName().isBlank();
    }

    /**
     * Проверка: название не может быть пустым
     */
    protected boolean validationFilmName(Film film) {
        return film.getName().isEmpty();
    }

    /**
     * Проверка: максимальная длина описания — 200 символов
     */
    protected boolean validationFilmDescription(Film film) {
        return film.getDescription().isEmpty() || film.getDescription().length() > 200;
    }

    /**
     * Проверка: дата релиза — не раньше 28 декабря 1895 года
     */
    protected boolean validationFilmReleaseDate(Film film) {
        return film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28));
    }

    /**
     * Проверка: продолжительность фильма должна быть положительной
     */
    protected boolean validationFilmDuration(Film film) {
        return film.getDuration() <= 0;
    }

    /**
     * Проверка: id не может быть меньше или равен нулю
     */
    public boolean validationId(long id) {
        return id <= 0;
    }
}
