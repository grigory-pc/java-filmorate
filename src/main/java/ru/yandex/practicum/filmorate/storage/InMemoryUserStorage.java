package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

/**
 * Реализация интерфейса для хранения пользователей
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    protected long id;
    private final Validator validator;

    private final List<User> users = new ArrayList<>();

    /**
     * Добавление пользователя в список
     */
    public User add(User user) {
        try {
            if (validator.validationUserEmail(user)) {
                throw new ValidationException("Необходимо указать email и email должен содержать символ '@'");
            } else if (validator.validationUserLogin(user)) {
                throw new ValidationException("Необходимо указать login и login не должен содержать пробелов");
            } else if (validator.validationUserBirthday(user)) {
                throw new ValidationException("Некорректно указана дата рождения. Она не может быть в будущем");
            } else {
                if (validator.validationUserName(user)) {
                    user.setName(user.getLogin());
                }
                id = generateId();
                user.setId(id);
                user.setFriends(new HashSet<>());
                users.add(user);
            }
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
        return user;
    }

    /**
     * Обновление пользователя в списке
     */
    public User update(User user) {
        try {
            if (validator.validationUserEmail(user)) {
                throw new ValidationException("Необходимо указать email и email должен содержать символ '@'");
            } else if (validator.validationUserLogin(user)) {
                throw new ValidationException("Необходимо указать login и login не должен содержать пробелов");
            } else if (validator.validationUserBirthday(user)) {
                throw new ValidationException("Некорректно указана дата рождения. Она не может быть в будущем");
            } else {
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getId() == user.getId()) {
                        User userExisting = users.get(i);
                        userExisting.setName(user.getName());
                        userExisting.setBirthday(user.getBirthday());
                        userExisting.setEmail(user.getEmail());
                        userExisting.setLogin(user.getLogin());
                        users.set(i, userExisting);
                        return userExisting;
                    }

                }
                throw new NotFoundException(String.format("Пользователь с id %d не найден",
                        user.getId()));
            }
        } catch (ValidationException | NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * Получение пользователя из списка по ID
     */
    public User getUserById(long userId) {
        return users.stream()
                .filter(p -> p.getId() == (userId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден",
                        userId)));
    }

    /**
     * Получение списка пользователей
     */
    public List<User> getUserAll() {
        return users;
    }

    /**
     * Создание нового ID для добавления нового пользователя в писок
     */
    long generateId() {
        return ++id;
    }

}