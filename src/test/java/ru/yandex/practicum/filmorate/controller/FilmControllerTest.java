package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.impl.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.impl.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    private FilmController controller;
    private Validator validator;
    private UserStorage userStorage;
    private FilmStorage filmStorage;

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryUserStorage();
        filmStorage = new InMemoryFilmStorage();
        controller = new FilmController(new FilmService(filmStorage, userStorage));
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldCreateFilmWithNullName() {
        Film film = getTestFilm();
        film.setName("   ");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Валидация некорректна");

        film.setName("");

        Set<ConstraintViolation<Film>> violations2 = validator.validate(film);
        assertEquals(1, violations2.size(), "Валидация некорректна");
    }

    @Test
    public void shouldCreateFilmWithLongDescription() {
        Film film = getTestFilm();
        String str = film.getDescription();
        for (int i = 0; i <= 10; i++) {
            str = str + " " + str;
        }
        film.setDescription(str);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Валидация некорректна");
    }

    @Test
    public void shouldCreateFilmWithNegativeDuration() {
        Film film = getTestFilm();
        film.setDuration(-1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Валидация некорректна");
    }

    @Test
    public void shouldCreateFilmWithOldDate() {
        Film film = getTestFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 28).minusDays(100));
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> controller.create(film));

        assertEquals("Фильм не прошел валидацию", exception1.getMessage());
    }

    @Test
    public void shouldUpdateFilmWithNullName() throws ValidationException {
        Film film = getTestFilm();
        controller.create(film);
        Film newFilm = getUpdatedTestFilm();
        newFilm.setName("   ");

        Set<ConstraintViolation<Film>> violations = validator.validate(newFilm);
        assertEquals(1, violations.size(), "Валидация некорректна");

        newFilm.setName("");
        Set<ConstraintViolation<Film>> violations2 = validator.validate(newFilm);
        assertEquals(1, violations2.size(), "Валидация некорректна");
    }

    @Test
    public void shouldUpdateFilmWithLongDescription() throws ValidationException {
        Film film = getTestFilm();
        controller.create(film);
        Film newFilm = getUpdatedTestFilm();

        String str = newFilm.getDescription();
        for (int i = 0; i <= 10; i++) {
            str = str + " " + str;
        }
        newFilm.setDescription(str);

        Set<ConstraintViolation<Film>> violations = validator.validate(newFilm);
        assertEquals(1, violations.size(), "Валидация некорректна");
    }

    @Test
    public void shouldUpdateFilmWithNegativeDuration() {
        Film film = getTestFilm();
        controller.create(film);
        Film newFilm = getUpdatedTestFilm();

        newFilm.setDuration(-1);
        Set<ConstraintViolation<Film>> violations = validator.validate(newFilm);
        assertEquals(1, violations.size(), "Валидация некорректна");
    }

    @Test
    public void shouldUpdateFilmWithOldDate() {
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
    public void shouldUpdateFilmWithNullId() throws NotFoundException {
        Film film = getTestFilm();
        controller.create(film);
        Film newFilm = getUpdatedTestFilm();
        newFilm.setId(0);

        final NotFoundException exception1 = assertThrows(
                NotFoundException.class,
                () -> controller.update(newFilm));

        assertEquals("Невозможно обновить фильм. Нужно или указать id, или создать запрос POST", exception1.getMessage());
    }

    private static Film getTestFilm() {
        Film film = new Film();
        film.setName("new film");
        film.setDescription("a1b2c3 d4e5f6g7");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
        return film;
    }

    private static Film getUpdatedTestFilm() {
        Film film = new Film();
        film.setId(1);
        film.setName("Film Updated");
        film.setDescription("New film update description");
        film.setDuration(190);
        film.setReleaseDate(LocalDate.of(1989, 4, 17));
        return film;
    }
}
