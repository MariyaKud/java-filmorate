package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
public class jdbcUserStorage implements Storage<User> {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public User save(User user) {
        String sqlQuery;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource map = new MapSqlParameterSource();

        if (user.getId() == 0) {
            map.addValue("email", user.getEmail());
            map.addValue("login", user.getLogin());
            map.addValue("name", user.getName());
            map.addValue("birthday", user.getBirthday());

            sqlQuery = "insert into USERS (EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY) " +
                       "values (:email, :login, :name, :birthday)";
        } else if (findById(user.getId()).isPresent()) {
            sqlQuery = "UPDATE USERS SET " +
                       "EMAIL= :email, USER_LOGIN= :login, USER_NAME= :name, BIRTHDAY= :birthday " +
                       "WHERE USER_ID=:id";

            map.addValue("id", user.getId());
            map.addValue("email", user.getEmail());
            map.addValue("login", user.getLogin());
            map.addValue("name", user.getName());
            map.addValue("birthday", user.getBirthday());
        } else {
            return null;
        }

        jdbcOperations.update(sqlQuery, map, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return user;
    }

    @Override
    public List<User> getAll() {
        final String sqlQuery = "select * from USERS ";

        return jdbcOperations.query(sqlQuery, new UserRowMapper());
    }

    @Override
    public List<User> getPopular(int size) {

        final String sqlQuery = "SELECT u.USER_ID, u.USER_LOGIN, u.BIRTHDAY, u.EMAIL, u.USER_NAME " +
                                "FROM USERS u " +
                                "LEFT JOIN (SELECT f.FRIEND_ID, count(f.FRIEND_ID) AS ord " +
                                "           FROM FRIENDS f " +
                                "           GROUP BY f.FRIEND_ID) " +
                                "           o ON u.USER_ID = o.FRIEND_ID " +
                                "order BY o.ord DESC " +
                                "LIMIT :size";

        return jdbcOperations.query(sqlQuery, Map.of("size", size), new UserRowMapper());
    }

    @Override
    public Optional<User> findById(Long id) {

        final String sqlQuery = "select *" +
                                "from USERS " +
                                "where USER_ID = :userId ";

        final List<User> users = jdbcOperations.query(sqlQuery, Map.of("userId", id), new UserRowMapper());

        if (users.size() != 1) {
            return  Optional.empty();
        }

        return Optional.ofNullable(users.get(0));
    }

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
