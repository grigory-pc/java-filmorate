package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

/**
 * Интерфейс для хранения пользователей
 */
@Component
public interface UserStorage {
    User add(User user);

    List<User> getUserAll();

    User getUserById(long userId);
}
