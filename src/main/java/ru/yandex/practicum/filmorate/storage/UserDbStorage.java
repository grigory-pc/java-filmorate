package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Реализация интерфейса для хранения пользователей
 */
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private static final String FIND_ALL_USERS = "SELECT * " +
            "FROM users";
    private static final String FIND_USER_BY_ID = "SELECT * " +
            "FROM users " +
            "WHERE id = ?";
    private static final String UPDATE_USER = "UPDATE users SET " +
            "email = ?, login = ?, name = ?, birthday = ? " +
            "WHERE id = ?";
    private static final String FIND_USER_FRIENDS_BY_ID = "SELECT u.* " +
            "FROM friendship fs " +
            "LEFT JOIN users u ON fs.user2_id = u.id " +
            "WHERE fs.user1_id = ? ";
    private static final String FIND_FRIENDS_ID_BY_USER_ID = "SELECT user2_id " +
            "FROM friendship " +
            "WHERE user1_id = ? ";
    private static String DELETE_USER = "DELETE FROM users " +
            "WHERE id = ?";
    private static String CHECK_FRIENDSHIP = "SELECT status " +
            "FROM friendship " +
            "WHERE user1_id = ? " +
            "AND user1_id = ?";
    private static final String UPDATE_FRIENDSHIP = "UPDATE friendship SET " +
            "status = ? " +
            "WHERE id = ?";
    private static String INSERT_FRIENDSHIP = "INSERT INTO friendship (user1_id, user2_id, status) " +
            "VALUES (?, ?, ?)";
    private static String DELETE_FRIEND = "DELETE FROM friendship " +
            "WHERE user1_id = ? " +
            "AND user2_id = ?";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Получение списка пользователей
     */
    @Override
    public List<User> getUserAll() {
        return jdbcTemplate.query(FIND_ALL_USERS, this::mapRowToUser);
    }

    /**
     * Получение пользователя по ID
     */
    @Override
    public User getUserById(long userId) {
        return jdbcTemplate.queryForObject(FIND_USER_BY_ID, this::mapRowToUser, userId);
    }

    /**
     * Добавление пользователя
     */
    @Override
    public User add(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());

        return user;
    }

    /**
     * Обновление данных пользователя
     */
    @Override
    public User update(User user) {
        jdbcTemplate.update(UPDATE_USER
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());
        return getUserById(user.getId());
    }

    /**
     * Удаление пользователя
     */
    @Override
    public boolean delete(User user) {
        return jdbcTemplate.update(DELETE_USER, user.getId()) > 0;
    }

    /**
     * Получение списка друзей пользователя
     */
    @Override
    public List<User> getFriendList(long id) {
        return jdbcTemplate.query(FIND_USER_FRIENDS_BY_ID, this::mapRowToUser, id);
    }

    /**
     * Добавление пользователя в друзья другого пользователя
     */
    @Override
    public boolean addFriend(long id, long friendId) {
        SqlRowSet statusRowsUser1 = jdbcTemplate.queryForRowSet(CHECK_FRIENDSHIP, id, friendId);
        SqlRowSet statusRowsUser2 = jdbcTemplate.queryForRowSet(CHECK_FRIENDSHIP, friendId, id);
        if (statusRowsUser1.toString().equals("Confirmed")) {
            return false;
        } else if (statusRowsUser1.toString().equals("Not Confirmed")) {
            return false;
        } else if (statusRowsUser2.toString().equals("Confirmed")) {
            return false;
        } else if (statusRowsUser2.toString().equals("Not Confirmed")) {
            jdbcTemplate.update(UPDATE_FRIENDSHIP, "Confirmed", id);
            return true;
        } else {
            jdbcTemplate.update(INSERT_FRIENDSHIP, id, friendId, "Not Confirmed");
            return true;
        }
    }

    /**
     * Удаление пользователя из друзей
     */
    @Override
    public boolean deleteFriend(long id, long friendId) {
        return jdbcTemplate.update(DELETE_FRIEND, id, friendId) > 0;
    }

    /**
     * Получение списка id друзей пользователя
     */
    @Override
    public Set<Long> getFriendsIdListByUserId(long id) {
        return new HashSet<>(jdbcTemplate.query(FIND_FRIENDS_ID_BY_USER_ID, (rs, friend_id) -> rs.getLong("user2_id"), id));
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}