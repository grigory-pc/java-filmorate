package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    public final List<User> users = new ArrayList<>();

    @GetMapping()
    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());

        return users;
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        log.trace(String.valueOf(user));

        try {
            if (validationEmail(user)) {
                throw new ValidationException("Необходимо указать email и email должен содержать символ '@'");
            } else if (validationLogin(user)) {
                throw new ValidationException("Необходимо указать login и login не должен содержать пробелов");
            } else if (validationBirthday(user)) {
                throw new ValidationException("Некорректно указана дата рождения. Она не может быть в будущем");
            } else if (validationName(user)) {
                user.setName(user.getLogin());
            } else {
                users.add(user);
            }
        } catch (ValidationException e) {
            log.warn(e.getMessage());
        }
        log.info("Пользователь " + user.getName() + " добавлен");

        return user;
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        try {
            if (validationEmail(user)) {
                throw new ValidationException("Необходимо указать email и email должен содержать символ '@'");
            } else if (validationLogin(user)) {
                throw new ValidationException("Необходимо указать login и login не должен содержать пробелов");
            } else if (validationBirthday(user)) {
                throw new ValidationException("Некорректно указана дата рождения. Она не может быть в будущем");
            } else if (validationName(user)) {
                user.setName(user.getLogin());
            } else {
                users.add(user);
            }
        } catch (ValidationException e) {
            log.warn(e.getMessage());
        }
        log.info("Информация о пользователе обновлена");

        return user;
    }

    private boolean validationEmail(User user) {
        return user.getEmail().isEmpty() || !user.getEmail().contains("@");
    }

    private boolean validationLogin(User user) {
        return user.getLogin().isEmpty() || user.getLogin().contains(" ");
    }

    private boolean validationBirthday(User user) {
        return user.getBirthday().isAfter(LocalDate.now());
    }

    private boolean validationName(User user) {
        return user.getName().isBlank();
    }
}
