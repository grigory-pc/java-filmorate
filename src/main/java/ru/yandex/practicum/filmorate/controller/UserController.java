package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;


/**
 * Основной контроллер для работы с пользователями
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Возвращает список всех пользователей
     */
    @GetMapping()
    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", userService.getUserAll().size());

        return userService.getUserAll();
    }

    /**
     * Возвращает пользователя по ID
     *
     * @param id объекта пользователя
     * @return объект пользователя
     */
    @GetMapping("/{id}")
    public User getUserById(@Valid @PathVariable long id) {
        log.debug("Поиск пользователя: {}", userService.getUserById(id).getName());

        return userService.getUserById(id);
    }

    /**
     * Создаёт объект пользователя
     *
     * @return возвращает объект пользователя, который был создан
     */
    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        log.trace(String.valueOf(user));

        return userService.add(user);
    }

    /**
     * Обновляет данные пользователя
     *
     * @return возвращает обновленный объект пользователя
     */
    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        log.trace(String.valueOf(user));

        return userService.update(user);
    }

    /**
     * Удаляет пользователя из списка
     *
     * @param id объекта пользователя, который удаляет из друзей
     * @return id объекта пользователя
     */
    @DeleteMapping("/{id}")
    public long deleteFriend(@Valid @PathVariable long id) {
        log.trace(String.valueOf(id));

        userService.delete(id);

        log.info("Пользователь удален");

        return id;
    }

    /**
     * Добавляет пользователя в друзья
     *
     * @param id       объекта пользователя, который добавляет в друзья
     * @param friendId объекта пользователя, которого добавляют в друзья
     * @return friendId объекта пользователя, которого добавляют в друзья
     */
    @PutMapping("/{id}/friends/{friendId}")
    public long updateFriend(@Valid @PathVariable long id, @PathVariable long friendId) {
        log.trace(String.valueOf(id));
        log.trace(String.valueOf(friendId));

        userService.addFriend(id, friendId);
        userService.addFriend(friendId, id);

        log.info("Информация о пользователе обновлена");

        return friendId;
    }

    /**
     * Удаляет пользователя из списка друзей
     *
     * @param id       объекта пользователя, который удаляет из друзей
     * @param friendId объекта пользователя, которого удаляют из друзья
     * @return friendId объекта пользователя
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    public long deleteFriend(@Valid @PathVariable long id, @PathVariable long friendId) {
        log.trace(String.valueOf(id));
        log.trace(String.valueOf(friendId));

        userService.deleteFriend(id, friendId);

        log.info("Пользователь удален из списка друзей");

        return friendId;
    }

    /**
     * Возвращает список друзей пользователя
     *
     * @param id объекта пользователя
     * @return список друзей пользователя
     */
    @GetMapping("/{id}/friends")
    public List<User> findAllFriends(@PathVariable long id) {
        log.debug("Текущее количество друзей: {}", userService.getAllFriends(id).size());

        return userService.getAllFriends(id);
    }

    /**
     * Возвращает список общих друзей двух пользователей
     *
     * @param id      объекта первого пользователя
     * @param otherId объекта второго пользователя
     * @return список общих друзей пользователей
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.debug("Текущее количество общих друзей: {}", userService.getCommonFriends(id, otherId));

        return userService.getCommonFriends(id, otherId);
    }
}
