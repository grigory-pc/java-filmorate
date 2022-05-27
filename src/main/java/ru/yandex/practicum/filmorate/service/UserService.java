package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Класс, ответственный за операции с пользователями
 */
@Service
@Slf4j
public class UserService {
    private long id;
    private final UserStorage userStorage;
    private final Validator validator;

    @Autowired
    public UserService(UserStorage userStorage, Validator validator) {
        this.userStorage = userStorage;
        this.validator = validator;
    }

    /**
     * Возвращает список всех пользователей
     */
    public List<User> getUserAll() {
        return userStorage.getUserAll();
    }

    /**
     * Возвращает пользователя по ID
     */
    public User getUserById(long userId) {
        return userStorage.getUserById(userId);
    }

    /**
     * Валидирует поля объекта и добавляет пользователя в список
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
                return userStorage.add(user);
            }
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * Валидирует поля объекта и обновляет пользователя в списке
     */
    public User update(User user) {
        List<User> users = userStorage.getUserAll();

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
                        return userStorage.update(i, userExisting);
                    }
                }
                throw new NotFoundException(String.format("Пользователь с id %d не найден", user.getId()));
            }
        } catch (ValidationException | NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * Удаление пользователя из списка
     */
    public long delete(long id) {
        List<User> users = userStorage.getUserAll();

        try {
            if (validator.validationId(id)) {
                throw new NotFoundException("id пользователя должен быть больше 0");
            } else {
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getId() == id) {
                        return userStorage.delete(i);
                    }
                }
                throw new NotFoundException(String.format("Пользователь с id %d не найден", id));
            }
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * Добавление пользователя в список друзей другого пользователя
     */
    public long addFriend(long userId, long friendId) {
        try {
            if (validator.validationId(userId) || validator.validationId(friendId)) {
                throw new NotFoundException("id пользователя или добавляемого друга должен быть больше 0");
            }
            userStorage.getUserById(userId).addFriend(friendId);
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
        return friendId;
    }

    /**
     * Удаление пользователя из списка друзей другого пользователя
     */
    public long deleteFriend(long userId, long friendId) {
        try {
            if (validator.validationId(userId) || validator.validationId(friendId)) {
                throw new NotFoundException("id пользователя или удаляемого друга должен быть больше 0");
            }
            userStorage.getUserById(userId).deleteFriend(friendId);
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
        return friendId;
    }

    /**
     * Получение списка друзей пользователя
     */
    public List<User> getAllFriends(long userId) {
        List<User> friends = new ArrayList<>();

        Set<Long> userFriends = userStorage.getUserById(userId).getFriends();

        for (long userFriendsId : userFriends) {
            friends.add(userStorage.getUserById(userFriendsId));
        }
        return friends;
    }

    /**
     * Получение списка общих друзей двух пользователей
     */
    public List<User> getCommonFriends(long userId, long otherId) {
        List<User> commonFriends = new ArrayList<>();

        try {
            if (validator.validationId(userId) || validator.validationId(otherId)) {
                throw new NotFoundException("id пользователя должен быть больше 0");
            }
            Set<Long> userFriends = new HashSet<>(userStorage.getUserById(userId).getFriends());
            Set<Long> otherFriends = new HashSet<>(userStorage.getUserById(otherId).getFriends());

            userFriends.retainAll(otherFriends);

            for (long userFriendsId : userFriends) {
                commonFriends.add(userStorage.getUserById(userFriendsId));
            }
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
        return commonFriends;
    }

    /**
     * Создание нового id для добавления нового пользователя в писок
     */
    long generateId() {
        return ++id;
    }
}

