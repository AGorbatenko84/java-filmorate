package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {    
    private final Map<Integer, User> users;
    private int id;

    public UserController() {
        this.id = 0;
        users = new HashMap<>();

    }

    @GetMapping("/users")
    public List<User> findAll() {
        List<User> usersList = new ArrayList<>();
        for (User user : users.values()) {
            usersList.add(user);
        }
        log.info("Список пользователей выведен");
        return usersList;
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) throws ValidationException {
        if (user.getEmail().equals(null) || !user.getEmail().contains("@") || user.getLogin().equals(null) ||
                user.getLogin().isBlank() || user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Данные пользователя {} не прошли валидацию", user.getLogin());
            throw new ValidationException("Данные пользователя не прошли валидацию");
        }
        if (user.getName().equals(null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Пользователю {} присвоено имя {}", user.getLogin(), user.getLogin());
        }
        this.id++;
        user.setId(this.id);
        users.put(this.id, user);
        log.info("Пользователь {} добавлен", user.getLogin());
        return user;
    }

    @PutMapping(value = "/users")
    public User update(@RequestBody User user) throws ValidationException {
        if (user.getEmail().equals(null) || !user.getEmail().contains("@") || user.getLogin().equals(null) ||
                user.getLogin().isBlank() || user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Данные пользователя {} не прошли валидацию", user.getLogin());
            throw new ValidationException("Данные пользователя не прошли валидацию");
        }
        if (user.getName().equals(null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Пользователю {} присвоено имя {}", user.getLogin(), user.getLogin());
        }
        int userId = user.getId();
        if (userId != 0 || users.containsKey(userId)) {
            users.put(userId, user);
            log.info("Данные пользователя {} изменены", user.getName());
        } else {
            log.info("Невозможно обновить пользователя. Нужно или указать id, или создать запрос POST");
            throw new ValidationException("Невозможно обновить пользователя. Нужно или указать id, или создать запрос POST");
        }
        return user;
    }
}
