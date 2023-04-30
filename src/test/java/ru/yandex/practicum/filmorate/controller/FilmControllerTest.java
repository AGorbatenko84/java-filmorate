package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class FilmControllerTest {
    private final FilmController controller = new FilmController();

    @Test
    public void shouldCreateFilmWithNullName() throws Exception, ValidationException {
        Film film = getTestFilm();
        film.setName("   ");

        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> controller.create(film));

        assertEquals("Фильм не прошел валидацию", exception1.getMessage());

        film.setName("");
        final ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> controller.create(film));
        assertEquals("Фильм не прошел валидацию", exception2.getMessage());
    }

    @Test
    public void shouldCreateFilmWithLongDescription() throws Exception, ValidationException {
        Film film = getTestFilm();
        String str = film.getDescription();
        for (int i = 0; i <= 10; i++) {
            str = str + " " + str;
        }
        film.setDescription(str);

        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> controller.create(film));

        assertEquals("Фильм не прошел валидацию", exception1.getMessage());
    }

    @Test
    public void shouldCreateFilmWithNegativeDuration() throws Exception, ValidationException {
        Film film = getTestFilm();
        film.setDuration(-1);
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> controller.create(film));

        assertEquals("Фильм не прошел валидацию", exception1.getMessage());
    }

    @Test
    public void shouldCreateFilmWithOldDate() throws Exception, ValidationException {
        Film film = getTestFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 28).minusDays(100));
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> controller.create(film));

        assertEquals("Фильм не прошел валидацию", exception1.getMessage());
    }


    @Test
    public void shouldUpdateFilmWithNullName() throws Exception, ValidationException {
        Film film = getTestFilm();
        controller.create(film);
        Film newFilm = getUpdatedTestFilm();
        newFilm.setName("   ");

        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> controller.update(newFilm));

        assertEquals("Фильм не прошел валидацию", exception1.getMessage());

        newFilm.setName("");
        final ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> controller.update(newFilm));
        assertEquals("Фильм не прошел валидацию", exception2.getMessage());
    }

    @Test
    public void shouldUpdateFilmWithLongDescription() throws Exception, ValidationException {
        Film film = getTestFilm();
        controller.create(film);
        Film newFilm = getUpdatedTestFilm();

        String str = newFilm.getDescription();
        for (int i = 0; i <= 10; i++) {
            str = str + " " + str;
        }
        newFilm.setDescription(str);

        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> controller.update(newFilm));

        assertEquals("Фильм не прошел валидацию", exception1.getMessage());
    }

    @Test
    public void shouldUpdateFilmWithNegativeDuration() throws Exception, ValidationException {
        Film film = getTestFilm();
        controller.create(film);
        Film newFilm = getUpdatedTestFilm();

        newFilm.setDuration(-1);
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> controller.update(newFilm));

        assertEquals("Фильм не прошел валидацию", exception1.getMessage());
    }

    @Test
    public void shouldUpdateFilmWithOldDate() throws Exception, ValidationException {
        Film film = getTestFilm();
        controller.create(film);
        Film newFilm = getUpdatedTestFilm();

        newFilm.setReleaseDate(LocalDate.of(1895, 12, 28).minusDays(100));
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> controller.update(newFilm));

        assertEquals("Фильм не прошел валидацию", exception1.getMessage());
    }

    @Test
    public void shouldUpdateFilmWithNullId() throws Exception, ValidationException {
        Film film = getTestFilm();
        controller.create(film);
        Film newFilm = getUpdatedTestFilm();
        newFilm.setId(0);

        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> controller.update(newFilm));

        assertEquals("Невозможно обновить фильм. Нужно или указать id, или создать запрос POST", exception1.getMessage());
    }


    private Film getTestFilm() {
        Film film = new Film();
        film.setName("new film");
        film.setDescription("a1b2c3 d4e5f6g7");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
        return film;
    }

    private Film getUpdatedTestFilm() {
        Film film = new Film();
        film.setId(1);
        film.setName("Film Updated");
        film.setDescription("New film update description");
        film.setDuration(190);
        film.setReleaseDate(LocalDate.of(1989, 4, 17));
        return film;
    }
}
