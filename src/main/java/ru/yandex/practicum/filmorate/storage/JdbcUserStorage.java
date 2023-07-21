package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Primary
public class JdbcUserStorage implements StorageUser {

    private final NamedParameterJdbcOperations jdbcOperations;

    private final UserRowMapper userMapper;

    @Override
    public List<User> getAll() {
        final String sqlQuery = "select * from USERS ";

        return jdbcOperations.query(sqlQuery, userMapper);
    }

    @Override
    public Optional<User> findById(Long id) {
        final String sqlQuery = "select *" +
                "from USERS " +
                "where USER_ID = :userId ";

        final List<User> users = jdbcOperations.query(sqlQuery, Map.of("userId", id), userMapper);

        if (users.size() != 1) {
            return  Optional.empty();
        }

        return Optional.ofNullable(users.get(0));
    }

    @Override
    public User save(User user) {
        String sqlQuery;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("email", user.getEmail());
        map.addValue("login", user.getLogin());
        map.addValue("name", user.getName());
        map.addValue("birthday", user.getBirthday());

        if (user.getId() == 0) {
            sqlQuery = "insert into USERS (EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY) " +
                       "values (:email, :login, :name, :birthday)";

        } else if (findById(user.getId()).isPresent()) {
            sqlQuery = "UPDATE USERS SET " +
                       "EMAIL= :email, USER_LOGIN= :login, USER_NAME= :name, BIRTHDAY= :birthday " +
                       "WHERE USER_ID=:id";

            map.addValue("id", user.getId());

        } else {
            return null;
        }

        jdbcOperations.update(sqlQuery, map, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return user;
    }

    @Override
    public List<User> getFriends(Long id) {
        final String sqlQuery = "SELECT u.USER_ID, u.USER_LOGIN, u.USER_NAME, u.EMAIL, u.BIRTHDAY " +
                "FROM FRIENDS f " +
                "JOIN USERS u ON f.FRIEND_ID = u.USER_ID " +
                "WHERE f.USER_ID = :id";

        return jdbcOperations.query(sqlQuery, Map.of("id", id), userMapper);
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        final String sqlQuery = "SELECT u.USER_ID, u.USER_LOGIN, u.USER_NAME, u.EMAIL, u.BIRTHDAY " +
                                "FROM FRIENDS f JOIN FRIENDS f2 ON f.FRIEND_ID = f2.FRIEND_ID " +
                                "JOIN USERS u ON f.FRIEND_ID = u.USER_ID " +
                                "WHERE f.USER_ID = :id AND f2.USER_ID = :otherId";

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);
        map.addValue("otherId", otherId);

        return jdbcOperations.query(sqlQuery, map, userMapper);
    }

    @Component
    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getLong("USER_ID"),
                    rs.getString("USER_LOGIN"),
                    rs.getString("USER_NAME"),
                    rs.getString("EMAIL"),
                    rs.getDate("BIRTHDAY").toLocalDate()
            );
        }
    }
}
