package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

/**
 * Объект пользователя
 */
@Data
@Builder
public class User {
    private long id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    private String friendship;
    @PastOrPresent
    private LocalDate birthday;
    private Set<Long> friends;

    /**
     * Добавление пользователя в список друзей
     */
    public long addFriend(long friendId) {
        if (!friends.contains(friendId)) {
            friends.add(friendId);
        }
        return friendId;
    }

    /**
     * Удаление пользователя из списка друзей
     */
    public long deleteFriend(long friendId) {
        if (friends.contains(friendId)) {
            friends.remove(friendId);
        }
        return friendId;
    }
}
