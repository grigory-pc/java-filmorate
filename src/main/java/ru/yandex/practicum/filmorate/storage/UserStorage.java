package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

/**
 * Интерфейс для хранения пользователей
 */
@Component
public interface UserStorage {
    List<User> getUserAll();

    User getUserById(long userId);

    User add(User user);

    User update(User user);

    boolean delete(User user);

    List<User> getFriendList(long id);

    boolean addFriend(long id, long friendId);

    boolean deleteFriend(long id, long friendId);

    Set<Long> getFriendsIdListByUserId(long id);
}