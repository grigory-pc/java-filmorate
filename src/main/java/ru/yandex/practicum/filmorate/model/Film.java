package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Set;

/**
 * Объект фильма
 */
@Data
@Builder
public class Film {
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @PastOrPresent
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    private Integer duration;
    private Set<Long> likes;

    /**
     * Добавление пользователя в список лайков
     */
    public long addLike(long userId) {
        if (!likes.contains(userId)) {
            likes.add(userId);
        }
        return userId;
    }

    /**
     * Удаление пользователя из списка лайков
     */
    public long deleteLike(long userId) {
        if (!likes.contains(userId)) {
            likes.remove(userId);
        }
        return userId;
    }
}
