package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private int rate;
    private Mpa mpa;
    @NotBlank
    private String description;
    @PastOrPresent
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    private Integer duration;
    private Set<Long> likes;
    private List<Genres> genres;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("rate", rate);
        values.put("mpa_id", mpa.getId());
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        return values;
    }
}
