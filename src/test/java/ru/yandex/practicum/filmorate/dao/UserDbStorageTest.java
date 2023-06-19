package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;


    @Test
    void shouldSave() {
        User user = User.builder()
                .email("test@test.com")
                .login("TestLogin1")
                .name("TestName1")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();

        User saveUser = userDbStorage.addUser(user);
        assertEquals(user, saveUser);
    }

    @Test
    void shouldUpdate() {
        User user = User.builder()
                .email("test2@test.com")
                .login("TestLogin2")
                .name("TestName2")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();

        final User saveUser = userDbStorage.addUser(user);
        saveUser.setEmail("test3@test.com");
        saveUser.setLogin("TestLogin3");
        saveUser.setName("TestName3");
        saveUser.setBirthday(LocalDate.of(1997, 9, 9));

        final User updateUser = userDbStorage.updateUser(saveUser);

        assertEquals(saveUser, updateUser);
    }

    @Test
    void shouldGetById() {
        User user = User.builder()
                .email("test4@test.com")
                .login("TeslLogin4")
                .name("TestName4")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();

        Long userId = userDbStorage.addUser(user).getId();
        User testUser = userDbStorage.getUserById(userId);

        assertEquals(user, testUser);
    }

    @Test
    void shouldGetAll() {
        User user1 = User.builder()
                .email("test12@test.com")
                .login("TeslLogin12")
                .name("TestName12")
                .birthday(LocalDate.of(1990, 9, 9))
                .build();
        User user2 = User.builder()
                .email("test13@test.com")
                .login("TeslLogin13")
                .name("TestName13")
                .birthday(LocalDate.of(1991, 9, 9))
                .build();
        userDbStorage.addUser(user1);
        userDbStorage.addUser(user2);
        List<User> getUsers = userDbStorage.getListUsers();
        assertEquals(3, getUsers.size());
    }
}
