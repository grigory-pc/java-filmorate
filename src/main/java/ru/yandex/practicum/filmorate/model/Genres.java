package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

/**
 * Объект жанров
 */
@Data
@Builder
public class Genres {
    private int id;
    private String name;
}
