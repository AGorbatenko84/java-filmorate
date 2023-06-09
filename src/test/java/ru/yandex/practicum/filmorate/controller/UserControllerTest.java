package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.impl.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {
    private UserController controller;
    private Validator validator;
    private UserStorage userStorage;

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryUserStorage();
        controller = new UserController(new UserService(userStorage));
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldCreateUserWithNullEmail() {
        User user = getTestUser();
        user.setEmail("");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Валидация некорректна");

        user.setName("assdaasdsad");
        Set<ConstraintViolation<User>> violations2 = validator.validate(user);
        assertEquals(1, violations2.size(), "Валидация некорректна");
    }

    @Test
    public void shouldCreateUserWithNullLogin() {
        User user = getTestUser();
        user.setLogin("    ");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Валидация некорректна");

        user.setLogin("");
        Set<ConstraintViolation<User>> violations2 = validator.validate(user);
        assertEquals(1, violations2.size(), "Валидация некорректна");
    }

    @Test
    public void shouldCreateUserWithLaterDate() {
        User user = getTestUser();
        user.setBirthday(LocalDate.now().plusDays(100));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Валидация некорректна");
    }

    @Test
    public void shouldCreateUserWithNullName() {
        User user1 = getTestUser();
        user1.setName("    ");
        controller.create(user1);

        assertEquals(user1.getLogin(), user1.getName());

        User user2 = getTestUser();
        user2.setName("");
        controller.create(user2);
        assertEquals(user2.getLogin(), user2.getName());
    }

    @Test
    public void shouldUpdateUserWithNullEmail() {
        User user = getTestUser();
        controller.create(user);
        User newUser = getUpdatedTestUser();

        newUser.setEmail("");

        Set<ConstraintViolation<User>> violations = validator.validate(newUser);
        assertEquals(1, violations.size(), "Валидация некорректна");

        newUser.setName("assdaasdsad");
        Set<ConstraintViolation<User>> violations2 = validator.validate(newUser);
        assertEquals(1, violations2.size(), "Валидация некорректна");
    }

    @Test
    public void shouldUpdateUserWithNullLogin() {
        User user = getTestUser();
        controller.create(user);
        User newUser = getUpdatedTestUser();

        newUser.setLogin("    ");

        Set<ConstraintViolation<User>> violations = validator.validate(newUser);
        assertEquals(1, violations.size(), "Валидация некорректна");

        newUser.setLogin("");
        Set<ConstraintViolation<User>> violations2 = validator.validate(newUser);
        assertEquals(1, violations2.size(), "Валидация некорректна");
    }

    @Test
    public void shouldUpdateUserWithLaterDate() throws Exception, ValidationException {
        User user = getTestUser();
        controller.create(user);
        User newUser = getUpdatedTestUser();

        newUser.setBirthday(LocalDate.now().plusDays(100));
        Set<ConstraintViolation<User>> violations2 = validator.validate(newUser);
        assertEquals(1, violations2.size(), "Валидация некорректна");
    }

    @Test
    public void shouldUpdateUserWithNullName() throws Exception, ValidationException {
        User user1 = getTestUser();
        controller.create(user1);
        User newUser1 = getUpdatedTestUser();
        newUser1.setName("    ");
        controller.update(newUser1);

        assertEquals(newUser1.getLogin(), newUser1.getName());

        User user2 = getTestUser();
        controller.create(user2);
        User newUser2 = getUpdatedTestUser();
        newUser2.setName("");
        controller.update(newUser2);
        assertEquals(newUser2.getLogin(), newUser2.getName());
    }

    private User getTestUser() {
        User user = User.builder()
                .name("User 1")
                .login("Login User 1")
                .email("fdfafa@mail.ru")
                .birthday(LocalDate.of(1984, 12, 1))
                .build();
        return user;
    }

    private User getUpdatedTestUser() {

        User user = User.builder()
                .id(1L)
                .name("User 2")
                .login("Login User 2")
                .email("retrtetewtwt@mail.ru")
                .birthday(LocalDate.of(1985, 10, 25))
                .build();
        return user;
    }
}
