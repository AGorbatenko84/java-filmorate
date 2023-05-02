package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
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
        log.info("Список фильмов выведен");
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Фильм не прошел валидацию");
            throw new ValidationException("Фильм не прошел валидацию");
        }
        this.id++;
        film.setId(this.id);
        films.put(this.id, film);
        log.info("Фильм {} добавлен", film.getName());
        return film;
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Фильм не прошел валидацию");
            throw new ValidationException("Фильм не прошел валидацию");
        }
        int filmId = film.getId();
        if (filmId != 0 && films.containsKey(filmId)) {
            films.put(filmId, film);
            log.info("Фильм {} изменен", film.getName());
        } else {
            log.info("Невозможно обновить фильм. Нужно или указать id, или создать запрос POST");
            throw new ValidationException("Невозможно обновить фильм. Нужно или указать id, или создать запрос POST");
        }
        return film;
    }

}
