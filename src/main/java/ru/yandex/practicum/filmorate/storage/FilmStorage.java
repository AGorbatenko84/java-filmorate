package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List getListFilms();

    List getListIds();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> getFilmById(Long id);

    void deleteFilmById(Long id);

    void addLikeToFilm(Film film, User user);

    void deleteLikeToFilm(Film film, User user);

    List getListBestFilms(int count);
}
