package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Primary
public class JdbcFriendsStorage implements StorageProperties {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public void add(Long id, Long propertyId) {
        String sqlQuery = "insert into FRIENDS (USER_ID, FRIEND_ID) values (:id, :propertyId)";

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);
        map.addValue("propertyId", propertyId);

        jdbcOperations.update(sqlQuery, map);
    }

    @Override
    public void delete(Long id, Long propertyId) {
        String sqlQuery = "DELETE FROM FRIENDS where USER_ID = :id  and FRIEND_ID = :propertyId";

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);
        map.addValue("propertyId", propertyId);

        jdbcOperations.update(sqlQuery, map);
        System.out.println("удалили");
    }

    @Override
    public int size(Long id) {
        final String sqlQuery = "select count(FRIEND_ID) as RESULT " +
                                "from FRIENDS " +
                                "where USER_ID = :id ";

        final List<Integer> row = jdbcOperations.query(sqlQuery, java.util.Map.of("id", id),
                                                       (rs, rowNum) -> rs.getInt("RESULT"));
        if (row.size() > 0) {
            return row.get(0);
        } else {
            return 0;
        }
    }

    @Override
    public Set<Long> getAll(Long id) {
        final String sqlQuery = "select FRIEND_ID " +
                                "from FRIENDS " +
                                "where USER_ID = :id ";

        final List<Long> ids = jdbcOperations.query(sqlQuery, java.util.Map.of("id", id),
                                                    (rs, rowNum) -> rs.getLong("FRIEND_ID"));

        return new HashSet<>(ids);
    }
}

