package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;


    @Test
    void shouldSave() {
        Mpa mpa = new Mpa();
        mpa.setId(1);

        Genre genre = new Genre();
        genre.setId(1);
        Film film = new Film();
        film.setName("тестовый фильм 1");
        film.setDescription("описание тестового фильма 1");
        film.setReleaseDate(LocalDate.of(1988, 11, 8));
        film.setDuration(107);
        film.setMpa(mpa);
        film.getGenres().add(genre);

        Film saveFilm = filmDbStorage.addFilm(film);
        assertEquals(saveFilm.getName(), film.getName());
        Assertions.assertTrue(saveFilm.getGenres().contains(genre));
    }

    @Test
    void shouldUpdate() {
        Mpa mpa = new Mpa();
        mpa.setId(1);

        Genre genre = new Genre();
        genre.setId(1);
        Film film = new Film();
        film.setName("тестовый фильм 2");
        film.setDescription("описание тестового фильма 2");
        film.setReleaseDate(LocalDate.of(1988, 11, 8));
        film.setDuration(120);
        film.setMpa(mpa);
        film.getGenres().add(genre);


        Film updateFilm = filmDbStorage.addFilm(film);
        Mpa updateRate = new Mpa();
        mpa.setId(2);


        Genre updateGenre = new Genre();
        genre.setId(3);

        updateFilm.setName("новый тестовый фильм 2");
        updateFilm.setDescription("новое описание тестового фильма 2");
        updateFilm.setReleaseDate(LocalDate.of(1993, 6, 14));
        updateFilm.setDuration(102);
        updateFilm.setMpa(updateRate);
        updateFilm.getGenres().add(updateGenre);

        Film updatedFilm = filmDbStorage.updateFilm(updateFilm);

        assertEquals(updatedFilm.getName(), updateFilm.getName());
        Assertions.assertTrue(updatedFilm.getGenres().contains(updateGenre));
    }

    @Test
    void shouldGetById() {
        Mpa mpa = new Mpa();
        mpa.setId(1);

        Genre genre = new Genre();
        genre.setId(1);

        Film film = new Film();
        film.setName("тестовый фильм 3");
        film.setDescription("описание тестового фильма 3");
        film.setReleaseDate(LocalDate.of(1950, 5, 12));
        film.setDuration(40);
        film.setMpa(mpa);
        film.getGenres().add(genre);

        Film saveFilm = filmDbStorage.addFilm(film);
        Film getFilm = filmDbStorage.getFilmById(saveFilm.getId()).get();

        assertEquals(getFilm.getName(), film.getName());
        assertEquals("G", getFilm.getMpa().getName());
        Assertions.assertTrue(getFilm.getGenres().contains(genre));
    }

    @Test
    void shouldAddLikeAndGetPopularFilm() {
        User user = User.builder()
                .email("Ffgdfgd@mail.ru")
                .login("Fkjdgmd")
                .name("Yssdfs")
                .birthday(LocalDate.of(1984, 12, 1))
                .build();
        userDbStorage.addUser(user);

        Mpa mpa = new Mpa();
        mpa.setId(3);

        Genre genre = new Genre();
        genre.setId(6);

        Film film = new Film();
        film.setName("тестовый фильм 5");
        film.setDescription("описание тестового фильма 5");
        film.setReleaseDate(LocalDate.of(2022, 12, 31));
        film.setDuration(120);
        film.setMpa(mpa);
        film.getGenres().add(genre);
        film.getLikes().add(user.getId());
        Film saveFilm = filmDbStorage.addFilm(film);


        List<Film> listFilm = filmDbStorage.getListBestFilms(2);
        assertEquals(listFilm.get(0).getName(), saveFilm.getName());
    }

    @Test
    void shouldGetAll() {
        Mpa mpa = new Mpa();
        mpa.setId(3);

        Genre genre = new Genre();
        genre.setId(4);

        Film film = new Film();
        film.setName("тестовый фильм 3");
        film.setDescription("описание тестового фильма 3");
        film.setReleaseDate(LocalDate.of(1950, 4, 13));
        film.setDuration(40);
        film.setMpa(mpa);
        film.getGenres().add(genre);

        filmDbStorage.addFilm(film);

        List<Film> getAll = filmDbStorage.getListFilms();
        assertEquals(1, getAll.size());
    }
}
