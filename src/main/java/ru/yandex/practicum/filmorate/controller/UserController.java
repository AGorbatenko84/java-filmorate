package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> users;
    private int id;

    public UserController() {
        this.id=0;
        users = new HashMap<>();

    }

    @GetMapping("/users")
    public List<User> findAll() {
        List<User> usersList = new ArrayList<>();
        for (User user:users.values()) {
            usersList.add(user);
        }
        log.info("Список пользователей выведен");
        return usersList;
    }

    @PostMapping(value = "/user")
    public User create(@RequestBody User user) throws ValidationException {
        if (user.getEmail().equals(null) || !user.getEmail().contains("@") || user.getLogin().equals(null) ||
               user.getLogin().isBlank() || user.getBirthday().isAfter(LocalDate.now())){
            log.info("Данные пользователя {} не прошли валидацию", user.getLogin());
            throw new ValidationException("Данные пользователя не прошли валидацию");
        }
        if (user.getName().equals(null)  || user.getName().isBlank()){
            user.setName(user.getLogin());
            log.info("Пользователю {} присвоено имя {}", user.getLogin(), user.getLogin());
        }
        this.id++;
        user.setId(this.id);
        users.put(this.id, user);
        log.info("Пользователь {} добавлен", user.getLogin());
        return user;
    }

    @PutMapping(value = "/film")
    public User update(@RequestBody User user) throws ValidationException {
        if (user.getEmail().equals(null) || !user.getEmail().contains("@") || user.getLogin().equals(null) ||
                user.getLogin().isBlank() || user.getBirthday().isAfter(LocalDate.now())){
            log.info("Данные пользователя {} не прошли валидацию", user.getLogin());
            throw new ValidationException("Данные пользователя не прошли валидацию");
        }
        if (user.getName().equals(null)  || user.getName().isBlank()){
            user.setName(user.getLogin());
            log.info("Пользователю {} присвоено имя {}", user.getLogin(), user.getLogin());
        }
        int userId = user.getId();
        if (userId!=0 || users.containsKey(userId)) {
            users.put(userId, user);
            log.info("Данные пользователя {} изменены", user.getName());
        } else{
            log.info("Невозможно обновить пользователя. Нужно или указать id, или создать запрос POST");
            throw new ValidationException("Невозможно обновить пользователя. Нужно или указать id, или создать запрос POST");
        }
        return user;
    }
}
