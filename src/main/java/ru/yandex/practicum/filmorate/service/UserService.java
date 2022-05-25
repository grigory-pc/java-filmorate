package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Класс, ответственный за операции с пользователями
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final Validator validator;
    private Set<Long> friends;

    /**
     * Добавление пользователя в список друзей другого пользователя
     */
    public long addFriend(long userId, long friendId) {
        friends = userStorage.getUserById(userId).getFriends();
        try {
            if (validator.validationId(userId) || validator.validationId(friendId)) {
                throw new NotFoundException("id пользователя или добавляемого друга должен быть больше 0");
            }
            if (!friends.contains(friendId)) {
                friends.add(friendId);
                if (userStorage.getUserById(friendId) != null)
                    System.out.println("Друг " + userStorage.getUserById(friendId).getName() + " добавлен в список друзей");
                else {
                    System.out.println("Друг добавлен в список друзей");
                }
            } else {
                System.out.println("Друг уже в списке друзей");
            }
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }

        return friendId;
    }

    /**
     * Удаление пользователя в друзья другого пользователя
     */
    public long deleteFriend(long userId, long friendId) {
        friends = userStorage.getUserById(userId).getFriends();

        try {
            if (validator.validationId(userId) || validator.validationId(friendId)) {
                throw new NotFoundException("id пользователя или удаляемого друга должен быть больше 0");
            }
            if (friends.contains(friendId)) {
                friends.remove(friendId);
                if (userStorage.getUserById(friendId) != null) {
                    System.out.println("Друг " + userStorage.getUserById(friendId).getName() + " удален из списка друзей");
                } else {
                    System.out.println("Друг удален из списка друзей");
                }
            } else {
                System.out.println("Друг отсутствует в списке друзей");
            }
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

            Set<Long> userFriends = userStorage.getUserById(userId).getFriends();
            Set<Long> otherFriends = userStorage.getUserById(otherId).getFriends();

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
}

