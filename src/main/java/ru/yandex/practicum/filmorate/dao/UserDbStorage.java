package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Qualifier("db")
@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getListUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> UserDbStorage.this.mapRowToUser(resultSet, rowNum));
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = User.builder()
                .id(resultSet.getLong("user_id"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .email(resultSet.getString("email"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
        return user;
    }

    @Override
    public List<Long> getListIds() {
        List<User> listUsers = getListUsers();
        List<Long> list = new ArrayList<>();
        for (User u : listUsers) {
            list.add(u.getId());
        }
        return list;
    }

    @Override
    public User addUser(User user) {
        String sql = "INSERT INTO users (name, login, email, birthday) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        User userBack = user;
        userBack.setId(keyHolder.getKey().longValue());
        return userBack;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET name=?, login=?, email=?, birthday=? WHERE user_id=?";
        jdbcTemplate.update(sql, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        return (User) jdbcTemplate.query(sql, new Object[]{id}, new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                        return UserDbStorage.this.mapRowToUser(resultSet, rowNum);
                    }
                })
                .stream()
                .findAny()
                .orElse(null);
    }

    @Override
    public Set<Long> getUserFriendsIdsById(Long id) {
        List<User> list = getListFriends(id);
        Set<Long> set = new HashSet<>();
        for (User u : list) {
            set.add(u.getId());
        }
        return set;
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        String sql = "INSERT INTO friends(user_id, friend_id) VALUES(?, ?)";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public List<User> getListFriends(Long id) {
        String sql = "SELECT u.user_id, u.name, u.login, u.email, u.birthday " +
                "FROM users AS u JOIN friends AS f ON f.friend_id = u.user_id WHERE f.user_id = ?";
        List<User> friends = jdbcTemplate.query(sql, ps -> ps.setLong(1, id),
                (resultSet, rowNum) -> mapRowToUser(resultSet, rowNum));
        return friends;
    }
}
