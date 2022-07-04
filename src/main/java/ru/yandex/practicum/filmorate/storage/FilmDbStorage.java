package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Реализация интерфейса для хранения фильмов
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private static final String FIND_ALL_FILMS = "SELECT * " +
            "FROM films";
    private static final String FIND_FILM_BY_ID = "SELECT * " +
            "FROM films " +
            "WHERE film_id = ?";
    private static final String UPDATE_FILM = "UPDATE films SET " +
            "name = ?, rate = ?, mpa_id = ?, description = ?, release_date = ?, duration = ? " +
            "WHERE film_id = ?";
    private static String DELETE_FILM = "DELETE FROM films " +
            "WHERE film_id = ?";
    private static String INSERT_FILM_LIKE = "INSERT INTO film_like (film_id, user_id) " +
            "VALUES(?, ?)";
    private static String DELETE_FILM_LIKE = "DELETE FROM film_like " +
            "WHERE film_id = ? " +
            "AND user_id = ?";
    private static final String FIND_POPULAR_FILMS_WITH_LIKES = "SELECT f.* " +
            "FROM films f " +
            "LEFT JOIN film_like fl ON fl.film_id = f.film_id " +
            "GROUP BY f.film_id " +
            "ORDER BY COUNT(fl.user_id) DESC " +
            "LIMIT ?";
    private static String INSERT_FILM_GENRE = "INSERT INTO film_genre (film_id, genre_id) " +
            "VALUES(?, ?)";
    private static final String FIND_MPA_BY_ID = "SELECT * " +
            "FROM mpa " +
            "WHERE mpa_id = ?";
    private static final String FIND_GENRES_BY_FILM_ID = "SELECT fg.genre_id, " +
            "g.name " +
            "FROM film_genre fg " +
            "LEFT JOIN genre g ON fg.genre_id = g.genre_id " +
            "WHERE fg.film_id = ?";
    private static final String DELETE_GENRES_BY_FILM_ID = "DELETE FROM film_genre " +
            "WHERE film_id = ?";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Получение списка фильмов
     */
    @Override
    public List<Film> getFilmAll() {
        return jdbcTemplate.query(FIND_ALL_FILMS, this::mapRowToFilm);
    }

    /**
     * Получение фильма по id
     */
    @Override
    public Film getFilmById(long filmId) {
        Film film = jdbcTemplate.queryForObject(FIND_FILM_BY_ID, this::mapRowToFilm, filmId);

        if (film.getGenres().size() == 0) {
            film.setGenres(null);
        }

        return film;
    }

    /**
     * Добавление фильма
     */
    @Override
    public Film add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());

        return updateGenres(film);
    }

    /**
     * Обновление данных фильма
     */
    @Override
    public Film update(Film film) {
        jdbcTemplate.update(UPDATE_FILM
                , film.getName()
                , film.getRate()
                , film.getMpa().getId()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getId());

        jdbcTemplate.update(DELETE_GENRES_BY_FILM_ID, film.getId());

        return updateGenres(film);
    }

    /**
     * Удаление фильма
     */
    @Override
    public boolean delete(Film film) {
        return jdbcTemplate.update(DELETE_FILM, film.getId()) > 0;
    }

    /**
     * Получение списка с определенным количеством популярных фильмов
     */
    @Override
    public List<Film> getPopularFilms(int count) {
        List<Film> films = jdbcTemplate.query(FIND_POPULAR_FILMS_WITH_LIKES, this::mapRowToFilm, count);

        return films;
    }

    /**
     * Добавление лайка фильму
     */
    @Override
    public boolean addLike(long userId, long filmId) {
        return (jdbcTemplate.update(INSERT_FILM_LIKE, userId, filmId)) > 0;
    }

    /**
     * Удалние лайка у фильма
     */
    @Override
    public boolean deleteLike(long filmId, long userId) {
        return (jdbcTemplate.update(DELETE_FILM_LIKE, filmId, userId)) > 0;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("name"))
                .rate(resultSet.getInt("rate"))
                .mpa(getSetMpa(resultSet))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .genres(getSetGenres(resultSet))
                .build();
    }

    private Mpa getSetMpa(ResultSet rs) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("mpa_id"));

        mpa.setName(jdbcTemplate.queryForObject(FIND_MPA_BY_ID, this::mapRowToMpa, mpa.getId()).getName());
        return mpa;
    }

    private Film updateGenres(Film film) {
        if (film.getGenres() != null) {
            if (film.getGenres().size() == 0) {
                return film;
            }
            film.getGenres().stream()
                    .map(Genres::getId)
                    .distinct()
                    .forEach(id -> jdbcTemplate.update(INSERT_FILM_GENRE, film.getId(), id));
        }
        return getFilmById(film.getId());
    }

    private List<Genres> getSetGenres(ResultSet rs) throws SQLException {
        long filmId = rs.getLong("film_id");

        return jdbcTemplate.query(FIND_GENRES_BY_FILM_ID, this::mapRowToGenre, filmId);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private Genres mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genres.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}