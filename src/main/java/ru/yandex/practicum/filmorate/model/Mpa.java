package ru.yandex.practicum.filmorate.model;

import lombok.*;

/**
 * Объект MPA
 */
@Getter
@Setter
@Builder
public class Mpa {
    private int id;
    private String name;

    public Mpa() {
    }

    public Mpa(int id, String name) {
        this.id = id;
        this.name = name;
    }
}