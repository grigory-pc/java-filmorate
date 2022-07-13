package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private static final String FIND_ALL_MPA = "SELECT * " +
            "FROM mpa";
    private static final String FIND_MPA_BY_ID = "SELECT * " +
            "FROM mpa " +
            "WHERE mpa_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getMpaAll() {
        return jdbcTemplate.query(FIND_ALL_MPA, this::mapRowToMpa);
    }

    @Override
    public Mpa getMpaById(int mpaId) {
        return jdbcTemplate.queryForObject(FIND_MPA_BY_ID, this::mapRowToMpa, mpaId);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}