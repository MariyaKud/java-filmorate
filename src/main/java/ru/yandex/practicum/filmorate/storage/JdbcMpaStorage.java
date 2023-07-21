package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Primary
public class JdbcMpaStorage implements StorageForRead<Mpa> {

    private final NamedParameterJdbcOperations jdbcOperations;

    private final MpaRowMapper mpaMapper;

    @Override
    public List<Mpa> getAll() {
        final String sqlQuery = "select MPA_ID, MPA_NAME " +
                                "from MPA ";

        return jdbcOperations.query(sqlQuery, mpaMapper);
    }

    @Override
    public Optional<Mpa> findById(Long id) {

        final String sqlQuery = "select MPA_ID, MPA_NAME " +
                "from MPA " +
                "where MPA_ID = :mpaId ";

        final List<Mpa> genres = jdbcOperations.query(sqlQuery,
                                                     java.util.Map.of("mpaId", id), mpaMapper);

        if (genres.size() != 1) {
            return Optional.empty();
        }

        return Optional.ofNullable(genres.get(0));
    }

    @Override
    public Set<Mpa> findByIds(Set<Long> ids) {

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);

        final String sqlQuery = "select MPA_ID, MPA_NAME " +
                "from MPA " +
                "where MPA_ID in (:ids)";

        List<Mpa> mpas = jdbcOperations.query(sqlQuery, parameters, mpaMapper);

        return new HashSet<>(mpas);
    }

    @Component
    private static class MpaRowMapper implements RowMapper<Mpa> {
        @Override
        public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Mpa(rs.getLong("MPA_ID"),
                    rs.getString("MPA_NAME"));
        }
    }
}
