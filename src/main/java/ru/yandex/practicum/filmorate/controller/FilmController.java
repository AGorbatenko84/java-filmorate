package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films;
    private int id;

    public FilmController() {
        this.id = 0;
        films = new HashMap<>();
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        List<Film> filmsList = new ArrayList<>();
        for (Film film : films.values()) {
            filmsList.add(film);
        }
        log.info("Список фильмов выведен");
        return filmsList;
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) throws ValidationException {
        if (film.getName().equals(null) || film.getName().isBlank() || film.getDescription().length() > 200 ||
                film.getDuration() < 0 || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Фильм не прошел валидацию");
            throw new ValidationException("Фильм не прошел валидацию");
        }
        this.id++;
        film.setId(this.id);
        films.put(this.id, film);
        log.info("Фильм {} добавлен", film.getName());
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) throws ValidationException {
        if (film.getName().equals(null) || film.getName().isBlank() || film.getDescription().length() > 200 ||
                film.getDuration() < 0 || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Фильм не прошел валидацию");
            throw new ValidationException("Фильм не прошел валидацию");
        }
        int filmId = film.getId();
        if (filmId != 0 & films.containsKey(filmId)) {
            films.put(filmId, film);
            log.info("Фильм {} изменен", film.getName());
        } else {
            log.info("Невозможно обновить фильм. Нужно или указать id, или создать запрос POST");
            throw new ValidationException("Невозможно обновить фильм. Нужно или указать id, или создать запрос POST");
        }
        return film;
    }

}
