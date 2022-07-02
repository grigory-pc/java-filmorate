package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

/**
 * Класс, ответственный за операции с пользователями
 */
@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final Validator validator;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, Validator validator) {
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
        try {
            if (validator.validationId(userId)) {
                throw new NotFoundException("id пользователя должен быть больше 0");
            }
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
        return userStorage.getUserById(userId);
    }

    /**
     * Валидирует поля объекта и добавляет пользователя
     */
    public User add(User user) {
        if (validationFieldsUser(user)) {
            if (validator.validationUserName(user)) {
                user.setName(user.getLogin());
            }
            return userStorage.add(user);
        }
        return user;
    }

    /**
     * Валидирует поля объекта и обновляет пользователя
     */
    public User update(User user) {
        try {
            if (validator.validationId(user.getId())) {
                throw new NotFoundException("id пользователя должен быть больше 0");
            }
            if (validationFieldsUser(user)) {
                userStorage.update(user);
            }
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
        return getUserById(user.getId());
    }

    /**
     * Удаление пользователя
     */
    public boolean delete(long id) {
        return userStorage.delete(getUserById(id));
    }

    /**
     * Добавление пользователя в список друзей другого пользователя
     */
    public long addFriend(long userId, long friendId) {
        try {
            if (validator.validationId(userId) || validator.validationId(friendId)) {
                throw new NotFoundException("id пользователя или добавляемого друга должен быть больше 0");
            }
            userStorage.addFriend(userId, friendId);
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
            userStorage.deleteFriend(userId, friendId);
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
        return userStorage.getFriendList(userId);
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
            Set<Long> userFriends = new HashSet<>(userStorage.getFriendsIdListByUserId(userId));
            Set<Long> otherFriends = new HashSet<>(userStorage.getFriendsIdListByUserId(otherId));

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
     * Валидация полей объекта
     */
    public boolean validationFieldsUser(User user) {
        try {
            if (validator.validationUserEmail(user)) {
                throw new ValidationException("Необходимо указать email и email должен содержать символ '@'");
            } else if (validator.validationUserLogin(user)) {
                throw new ValidationException("Необходимо указать login и login не должен содержать пробелов");
            } else if (validator.validationUserBirthday(user)) {
                throw new ValidationException("Некорректно указана дата рождения. Она не может быть в будущем");
            } else {
                return true;
            }
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }
}