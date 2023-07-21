package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Primary
public class JdbcGenreStorage implements StorageForRead<Genre> {

    private final NamedParameterJdbcOperations jdbcOperations;

    private final GenreRowMapper genreMapper;

    @Override
    public List<Genre> getAll() {
        final String sqlQuery = "select GENRE_ID, GENRE_NAME " +
                                "from GENRES ";

        return jdbcOperations.query(sqlQuery, genreMapper);
    }

    @Override
    public Optional<Genre> findById(Long id) {

        final String sqlQuery = "select GENRE_ID, GENRE_NAME " +
                                "from GENRES " +
                                "where GENRE_ID = :genreId ";

        final List<Genre> genres = jdbcOperations.query(sqlQuery,
                                    java.util.Map.of("genreId", id), genreMapper);

        if (genres.size() != 1) {
            return Optional.empty();
        }

        return Optional.ofNullable(genres.get(0));
    }

    @Override
    public Set<Genre> findByIds(Set<Long> ids) {

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);

        final String sqlQuery = "select GENRE_ID, GENRE_NAME " +
                "from GENRES " +
                "where GENRE_ID in (:ids)";

        List<Genre> genres = jdbcOperations.query(sqlQuery, parameters, genreMapper);

        return new HashSet<>(genres);
    }

    @Component
    private static class GenreRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Genre(rs.getLong("GENRE_ID"),
                             rs.getString("GENRE_NAME"));
        }
    }
}
