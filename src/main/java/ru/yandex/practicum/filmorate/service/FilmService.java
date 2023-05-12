package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;


@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> findAllFilms() {
        return filmStorage.getListFilms();
    }

    public void createFilm(@Valid Film film) throws ValidationException {
        validReleaseDate(film);        
        filmStorage.addFilm(film);
        log.info("Фильм {} добавлен", film.getName());
    }

    public void updateFilm(@Valid Film film) {
        validReleaseDate(film);
        long filmId = film.getId();
        if (filmId > 0 && filmStorage.getListIds().contains(filmId)) {
            filmStorage.updateFilm(film);
            log.info("Фильм {} изменен", film.getName());
        } else throw new NotFoundException("Невозможно обновить фильм. Нужно или указать id, или создать запрос POST");
    }

    public Film getFilmById(Long idFilm) {
        Film film = filmStorage.getFilmById(idFilm);
        if (film == null) {
            throw new NotFoundException("Фильм с таким id = " + idFilm + " не найден.");
        }
        return film;
    }

    public void createLike(Long idFilm, Long idUser) {
        if (idFilm <= 0 || !filmStorage.getListIds().contains(idFilm)) {
            throw new NotFoundException("Фильм c id = " + idFilm + " отсутствует");
        }
        if (idUser <= 0 || !userStorage.getListIds().contains(idUser)) {
            throw new NotFoundException("Пользователь c id = " + idUser + " отсутствует");
        }
        filmStorage.getFilmById(idFilm).setLike(idUser);
    }

    public void deleteLike(Long idFilm, Long idUser) {
        if (idFilm <= 0 || !filmStorage.getListIds().contains(idFilm)) {
            throw new NotFoundException("Фильм c id = " + idFilm + " отсутствует");
        }
        if (idUser <= 0 || !userStorage.getListIds().contains(idUser)) {
            throw new NotFoundException("Пользователь c id = " + idUser + " отсутствует");
        }
        filmStorage.getFilmById(idFilm).deleteLike(idUser);
    }

    public List<Film> getListBestFilms(int count) {
        return filmStorage.getListBestFilms(count);
    }
    
    private void validReleaseDate(Film film){
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Фильм не прошел валидацию");
            throw new ValidationException("Фильм не прошел валидацию");
        }
    }
}
