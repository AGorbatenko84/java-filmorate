package ru.yandex.practicum.filmorate.impl;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;


import java.util.*;
import java.util.stream.Collectors;

@Qualifier("memory")
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films;
    private long idFilm;

    public InMemoryFilmStorage() {
        this.films = new HashMap<>();
    }

    @Override
    public List<Film> getListFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public List<Long> getListIds() {
        return new ArrayList<>(films.keySet());
    }

    @Override
    public Film addFilm(Film film) {
        this.idFilm++;
        film.setId(this.idFilm);
        films.put(this.idFilm, film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void deleteFilmById(Long id) {
        films.remove(id);
    }

    @Override
    public List<Film> getListBestFilms(int count) {
        return films.values().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void addLikeToFilm(Film film, User user) {
        films.get(film.getId()).setLike(user.getId());
    }

    @Override
    public void deleteLikeToFilm(Film film, User user) {
        films.get(film.getId()).deleteLike(user.getId());
    }
}
