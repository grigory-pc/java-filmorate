package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

/**
 * Интерфейс для хранения пользователей
 */
@Component
public interface UserStorage {
    List<User> getUserAll();

    User getUserById(long userId);

    User add(User user);

    User update(User userExisting, User userForUpdate);

    User delete(User user);
}
