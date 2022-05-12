package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    public final HashMap<String, User> users = new HashMap<>();

    @GetMapping()
    public HashMap<String, User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());

        return users;
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        log.trace(String.valueOf(user));

        try {
            if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
                throw new InvalidEmailException("Необходимо указать email и email должен содержать символ '@'");
            } else if (!users.isEmpty() && users.containsKey(user.getEmail())) {
                throw new UserAlreadyExistException("Пользователь с таким email уже существует");
            } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
                throw new InvalidLoginException("Необходимо указать login и login не должен содержать пробелов");
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new InFutureBirthdayException("Некорректно указана дата рождения. Она не может быть в будущем");
            } else if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            } else {
                users.put(user.getEmail(), user);
            }
        } catch (UserAlreadyExistException | InvalidEmailException | InvalidLoginException |
                 InFutureBirthdayException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
        log.info("Пользователь " + user.getName() + " добавлен");

        return user;
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        try {
            if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
                throw new InvalidEmailException("Необходимо указать email и email должен содержать символ '@'");
            } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
                throw new InvalidLoginException("Необходимо указать login и login не должен содержать пробелов");
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new InFutureBirthdayException("Некорректно указана дата рождения. Она не может быть в будущем");
            } else if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            } else {
                users.put(user.getEmail(), user);
            }
        } catch (InvalidEmailException | InvalidLoginException | InFutureBirthdayException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
        log.info("Информация о пользователе обновлена");

        return user;
    }

}
