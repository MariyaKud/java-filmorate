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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Primary
public class JdbcFilmsStorage implements StorageFilm {

    private final NamedParameterJdbcOperations jdbcOperations;

    private final FilmRowMapper filmMapper;

    private final FilmGenreRowMapper genreMapper;

    @Override
    public Film save(Film film) {
        String sqlQuery;

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("name", film.getName());
        map.addValue("description", film.getDescription());
        map.addValue("release", film.getReleaseDate());
        map.addValue("duration", film.getDuration());
        map.addValue("mpa", film.getMpa().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        if (film.getId() == 0) {
            sqlQuery = "insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                       "values (:name, :description, :release, :duration, :mpa)";
        } else {
            sqlQuery = "UPDATE FILMS SET " +
                       "FILM_NAME= :name, DESCRIPTION= :description, RELEASE_DATE= :release, " +
                       "DURATION= :duration, MPA_ID= :mpa " +
                       "WHERE FILM_ID= :id";

            map.addValue("id", film.getId());
        }

        jdbcOperations.update(sqlQuery, map, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        deleteFilmGenres(film);
        setFilmGenres(film);

        return film;
    }

    @Override
    public List<Film> getAll() {
        final String sqlQuery = "SELECT f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, " +
                                       "f.RELEASE_DATE, f.DURATION, f.MPA_ID, m.MPA_NAME " +
                                "from FILMS f " +
                                "Left JOIN MPA m ON f.MPA_ID = m.MPA_ID";

        List<Film> films = jdbcOperations.query(sqlQuery, filmMapper);

        loadFilmsGenre(films);

        return films;
    }

    @Override
    public List<Film> getPopular(int size) {

        final String sqlQuery = "SELECT f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, " +
                                "   m.MPA_NAME" +
                                "   FROM FILMS f" +
                                "   LEFT JOIN LIKES l ON f.FILM_ID = l.FILM_ID" +
                                "   LEFT JOIN MPA m ON f.MPA_ID = m.MPA_ID" +
                                "   GROUP BY f.FILM_ID" +
                                "   order BY count(l.USER_ID) DESC" +
                                "   LIMIT :size";

        List<Film> films = jdbcOperations.query(sqlQuery, Map.of("size", size), filmMapper);

        loadFilmsGenre(films);

        return films;
    }

    @Override
    public Optional<Film> findById(Long id) {
        final String sqlQuery = "select f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, " +
                                "f.RELEASE_DATE, f.DURATION, f.MPA_ID, m.MPA_NAME " +
                                "from FILMS f " +
                                "Left JOIN MPA m ON f.MPA_ID = m.MPA_ID " +
                                "where FILM_ID = :filmId ";

        final List<Film> films = jdbcOperations.query(sqlQuery, Map.of("filmId", id), filmMapper);

        if (films.size() != 1) {
            return Optional.empty();
        }
        loadFilmsGenre(films);

        return Optional.ofNullable(films.get(0));
    }

    private void setFilmGenres(Film film) {

        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", film.getGenres().stream().map(Genre::getId).collect(Collectors.toList()));
        parameters.addValue("id", film.getId());

        final String sqlQuery = "insert into GENRE_FILMS " +
                                "SELECT f.FILM_ID, g.GENRE_ID FROM GENRES g JOIN FILMS f " +
                                "WHERE f.FILM_ID = :id and g.GENRE_ID in (:ids)";

        jdbcOperations.update(sqlQuery, parameters);
    }

    private void deleteFilmGenres(Film film) {
        String sqlQuery = "DELETE FROM GENRE_FILMS where FILM_ID = :filmId";

        jdbcOperations.update(sqlQuery, Map.of("filmId", film.getId()));
    }

    private void loadFilmsGenre(List<Film> films) {
		final List<Long> ids = films.stream()
                                    .map(Film::getId).collect(Collectors.toList());

        final Map<Long, Film> filmMap = films.stream()
                                             .collect(Collectors.toMap(Film::getId, film -> film, (a, b) -> b));

        final String sqlQuery = "SELECT f.FILM_ID, g.GENRE_ID, g.GENRE_NAME " +
                                "FROM GENRE_FILMS f " +
                                "LEFT JOIN GENRES g on f.GENRE_ID = g.GENRE_ID " +
                                "WHERE f.FILM_ID in (:ids)";

        List<FilmGenre> genres = jdbcOperations.query(sqlQuery, java.util.Map.of("ids", ids), genreMapper);

        for (FilmGenre fg : genres) {
            filmMap.get(fg.getFilmId()).setGenre(fg.getGenre());
        }
    }

    @Component
    private static class FilmRowMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Film(rs.getLong("FILM_ID"),
                    rs.getString("FILM_NAME"),
                    rs.getString("DESCRIPTION"),
                    rs.getDate("RELEASE_DATE").toLocalDate(),
                    rs.getInt("DURATION"),
                    new Mpa(rs.getLong("MPA_ID"),rs.getString("MPA_NAME"))
            );
        }
    }

    @Component
    private static class FilmGenreRowMapper implements RowMapper<FilmGenre> {
        @Override
        public FilmGenre mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new FilmGenre(rs.getLong("FILM_ID"),
                    new Genre(rs.getLong("GENRE_ID"), rs.getString("GENRE_NAME"))
            );
        }
    }
}
