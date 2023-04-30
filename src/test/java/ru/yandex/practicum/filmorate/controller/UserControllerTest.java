package ru.yandex.practicum.filmorate.controller;



import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    private final UserController controller = new UserController();

    @Test
    public void shouldCreateUserWithNullEmail() throws Exception, ValidationException {
        User user = getTestUser();
        user.setEmail("");

        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> controller.create(user));

        assertEquals("Данные пользователя не прошли валидацию", exception1.getMessage());

        user.setName("assdaasdsad");
        final ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> controller.create(user));
        assertEquals("Данные пользователя не прошли валидацию", exception2.getMessage());
    }

    @Test
    public void shouldCreateUserWithNullLogin() throws Exception, ValidationException {
        User user = getTestUser();
        user.setLogin("    ");

        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> controller.create(user));

        assertEquals("Данные пользователя не прошли валидацию", exception1.getMessage());

        user.setLogin("");
        final ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> controller.create(user));
        assertEquals("Данные пользователя не прошли валидацию", exception2.getMessage());
    }

    @Test
    public void shouldCreateUserWithLaterDate() throws Exception, ValidationException {
        User user = getTestUser();
        user.setBirthday(LocalDate.now().plusDays(100));
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> controller.create(user));

        assertEquals("Данные пользователя не прошли валидацию", exception1.getMessage());
    }

    @Test
    public void shouldCreateUserWithNullName() throws Exception, ValidationException {
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
    public void shouldUpdateUserWithNullEmail() throws Exception, ValidationException {
        User user = getTestUser();
        controller.create(user);
        User newUser = getUpdatedTestUser();

        newUser.setEmail("");

        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> controller.update(newUser));

        assertEquals("Данные пользователя не прошли валидацию", exception1.getMessage());

        newUser.setName("assdaasdsad");
        final ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> controller.update(newUser));
        assertEquals("Данные пользователя не прошли валидацию", exception2.getMessage());
    }

    @Test
    public void shouldUpdateUserWithNullLogin() throws Exception, ValidationException {
        User user = getTestUser();
        controller.create(user);
        User newUser = getUpdatedTestUser();

        newUser.setLogin("    ");

        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> controller.update(newUser));

        assertEquals("Данные пользователя не прошли валидацию", exception1.getMessage());

        newUser.setLogin("");
        final ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> controller.update(newUser));
        assertEquals("Данные пользователя не прошли валидацию", exception2.getMessage());
    }

    @Test
    public void shouldUpdateUserWithLaterDate() throws Exception, ValidationException {
        User user = getTestUser();
        controller.create(user);
        User newUser = getUpdatedTestUser();

        newUser.setBirthday(LocalDate.now().plusDays(100));
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> controller.update(newUser));

        assertEquals("Данные пользователя не прошли валидацию", exception1.getMessage());
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
        User user = new User();
        user.setName("User 1");
        user.setLogin("Login User 1");
        user.setEmail("fdfafa@mail.ru");
        user.setBirthday(LocalDate.of(1984, 12, 1));
        return user;
    }

    private User getUpdatedTestUser() {
        User user = new User();
        user.setId(1);
        user.setName("User 2");
        user.setLogin("Login User 2");
        user.setEmail("retrtetewtwt@mail.ru");
        user.setBirthday(LocalDate.of(1985, 10, 25));
        return user;
    }
}
