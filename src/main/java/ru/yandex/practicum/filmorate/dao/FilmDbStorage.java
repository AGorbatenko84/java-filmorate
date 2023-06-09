package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Qualifier("db")
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    private final GenreStorage genreStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
    }

    @Override
    public List<Film> getListFilms() {
        String sql = "SELECT * FROM films JOIN mpa ON films.mpa_id = mpa.mpa_id";
        List<Film> listFilm = jdbcTemplate.query(sql,
                (resultSet, rowNum) -> FilmDbStorage.this.mapRowToFilm(resultSet, rowNum));
        return listFilm;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("film_id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));
        loadGenres(film);
        loadLikes(film);
        int mpaId = resultSet.getInt("mpa_id");
        film.setMpa(loadMpa(mpaId));

        return film;
    }

    private Mpa loadMpa(int mpaId) {
        String sql = "SELECT * FROM mpa";
        List<Mpa> listMpa = jdbcTemplate.query(sql, (rs, rowNum) -> MpaDbStorage.mapRowToMpa(rs, rowNum));
        Mpa resultMpa = new Mpa();
        for (Mpa m : listMpa) {
            if (m.getId() == mpaId) {
                resultMpa = m;
            }
        }
        return resultMpa;
    }

    private void loadLikes(Film film) {
        String sqlQuery = "SELECT user_id FROM likes WHERE film_id = ?";
        List<Long> userIds = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getLong("user_id"), film.getId());
        userIds.forEach(id -> film.getLikes().add(id));
    }

    private void loadGenres(Film film) {
        String sql = "SELECT genre_id FROM film_genre WHERE film_id = ?";
        List<Integer> genreIds = jdbcTemplate.queryForList(sql, Integer.class, film.getId());

        for (Integer genreId : genreIds) {
            film.getGenres().add(genreStorage.findById(genreId).get());
        }
    }

    @Override
    public List<Long> getListIds() {
        List<Film> listFilms = getListFilms();
        List<Long> list = new ArrayList<>();
        for (Film f : listFilms) {
            list.add(f.getId());
        }
        return list;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlFilm = "INSERT INTO films (name, description, duration, release_date, mpa_id)"
                + " VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlFilm, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getDuration());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);


        Film filmBack = film;
        filmBack.setId(keyHolder.getKey().longValue());
        addFilmGenre(filmBack);

        return filmBack;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlFilm = "UPDATE films SET name=?, description=?, duration=?, release_date=?, mpa_id=? WHERE film_id=?";
        jdbcTemplate.update(sqlFilm,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());
        addFilmGenre(film);
        return film;
    }

    public void addFilmGenre(Film film) {
        if (film.getGenres() != null && film.getId() > 0) {

            String sql = "DELETE FROM film_genre WHERE film_id = ?";
            jdbcTemplate.update(sql, film.getId());

            List<Integer> ids = film.getGenres().stream().map(genre -> genre.getId()).collect(Collectors.toList());

            String sqlGenreId = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
            for (Integer id : ids) {
                jdbcTemplate.update(sqlGenreId, film.getId(), id);
            }
        }
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        String sql = "SELECT * FROM films WHERE film_id = ?";

        return jdbcTemplate.query(sql, new Object[]{id},
                        (resultSet, rowNum) -> FilmDbStorage.this.mapRowToFilm(resultSet, rowNum))
                .stream()
                .findAny();
    }

    @Override
    public void deleteFilmById(Long id) {
        String sql = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void addLikeToFilm(Film film, User user) {
        String sqlFilm = "INSERT INTO likes (user_id, film_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlFilm, user.getId(), film.getId());
    }

    @Override
    public void deleteLikeToFilm(Film film, User user) {
        String sql = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";
        jdbcTemplate.update(sql, user.getId(), film.getId());
    }

    @Override
    public List<Film> getListBestFilms(int count) {
        String sql = "SELECT COUNT(USER_ID) AS QUANTITY, F.*  \n" +
                "FROM LIKES \n" +
                "RIGHT JOIN FILMS F ON F.FILM_ID = LIKES.FILM_ID\n" +
                "GROUP BY F.FILM_ID \n" +
                "ORDER BY QUANTITY DESC\n" +
                "LIMIT ?";
        return jdbcTemplate.query(sql, new Object[]{count},
                (resultSet, rowNum) -> FilmDbStorage.this.mapRowToFilm(resultSet, rowNum));
    }
}
