//package ru.yandex.practicum.filmorate.storage;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.util.*;
//
///**
// * Реализация интерфейса для хранения пользователей
// */
//@Slf4j
//@Component
//public class InMemoryUserStorage implements UserStorage {
//    private final List<User> users = new ArrayList<>();
//
//    /**
//     * Добавление пользователя в список
//     */
//    public User add(User user) {
//        users.add(user);
//
//        return user;
//    }
//
//    /**
//     * Обновление пользователя в списке
//     */
//    public User update(User user) {
////        userExisting.setName(userForUpdate.getName());
////        userExisting.setBirthday(userForUpdate.getBirthday());
////        userExisting.setEmail(userForUpdate.getEmail());
////        userExisting.setLogin(userForUpdate.getLogin());
////
//        return user;
//    }
//
//    /**
//     * Удаление пользователя из списка
//     */
//    public boolean delete(User user) {
//        User userForDelete = user;
//
//        users.remove(user);
//
//        return users.contains(userForDelete);
//    }
//
//    /**
//     * Получение пользователя из списка по ID
//     */
//    public User getUserById(long userId) {
//        return users.stream()
//                .filter(p -> p.getId() == (userId))
//                .findFirst()
//                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден",
//                        userId)));
//    }
//
//    /**
//     * Получение списка пользователей
//     */
//    public List<User> getUserAll() {
//        return users;
//    }
//
//
//    @Override
//    public List<User> getFriendList(long id) {
//        return null;
//    }
//
//    @Override
//    public boolean addFriend(long id, long friendId) {
//        return false;
//    }
//
//    @Override
//    public boolean deleteFriend(long id, long friendId) {
//        return false;
//    }
//}