package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenresDbStorage implements GenresStorage {
    private static final String FIND_ALL_GENRE = "SELECT * " +
            "FROM genre";
    private static final String FIND_GENRE_BY_ID = "SELECT * " +
            "FROM genre " +
            "WHERE genre_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genres> getGenreAll() {
        return jdbcTemplate.query(FIND_ALL_GENRE, this::mapRowToGenre);
    }

    @Override
    public Genres getGenreById(int genreId) {
        return jdbcTemplate.queryForObject(FIND_GENRE_BY_ID, this::mapRowToGenre, genreId);
    }

    private Genres mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genres.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}