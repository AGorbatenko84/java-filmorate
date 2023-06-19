package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("db") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private UserStorage getUserStorage() {
        return this.userStorage;
    }

    public List<User> findAllUsers() {
        return userStorage.getListUsers();
    }

    public User createUser(@Valid User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Пользователю {} присвоено имя {}", user.getLogin(), user.getLogin());
        }
        User userBack = userStorage.addUser(user);
        log.info("Пользователь {} добавлен", user.getLogin());
        return userBack;
    }

    public User updateUser(@Valid User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Пользователю {} присвоено имя {}", user.getLogin(), user.getLogin());
        }
        long userId = user.getId();
        if (userId <= 0 || !userStorage.getListIds().contains(userId)) {
            log.info("Невозможно обновить пользователя. Нужно или указать id, или создать запрос POST");
            throw new NotFoundException("Невозможно обновить пользователя. Нужно или указать id, или создать запрос POST");
        }
        userStorage.updateUser(user);
        log.info("Данные пользователя {} изменены", user.getName());
        return user;
    }

    public User getUserById(long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с таким id = " + id + " не найден.");
        }
        return user;
    }

    public void createFriend(long id, long friendId) {
        if (!userStorage.getListIds().contains(id) || !userStorage.getListIds().contains(friendId)) {
            throw new NotFoundException("Такого id не существует");
        }
        userStorage.addFriend(id, friendId);
        log.info("Пользователь с id " + id + " добавил в друзья пользователя с id " + friendId);
    }

    public void deleteFriend(long id, long friendId) {
        if (id > 0 && friendId > 0) {
            userStorage.deleteFriend(id, friendId);
        } else throw new NotFoundException("Такого id не существует");
    }

    public List<User> getListFriends(long id) {
        List<User> friendsList = new ArrayList<>();
        if (id > 0) {
            friendsList = userStorage.getListFriends(id);
        } else throw new NotFoundException("Такого id не существует");
        return friendsList;
    }

    public List<User> getListCommonUsers(long id, long otherId) {
        List<User> commonUsers = new ArrayList<>();
        if (id > 0 && otherId > 0) {
            Set<Long> idsFriendsOfUser1 = userStorage.getUserFriendsIdsById(id);
            Set<Long> idsFriendsOfUser2 = userStorage.getUserFriendsIdsById(otherId);
            if (idsFriendsOfUser1 == null || idsFriendsOfUser2 == null) {
                throw new NotFoundException("Такого id не существует");
            }
            if (idsFriendsOfUser1.isEmpty() || idsFriendsOfUser2.isEmpty()) {
                return commonUsers;
            }
            Set<Long> commonUsersIds = filterCommonUsersIds(idsFriendsOfUser1, idsFriendsOfUser2);
            for (Long idUser : commonUsersIds) {
                commonUsers.add(userStorage.getUserById(idUser));
            }
        }
        return commonUsers;
    }

    private Set<Long> filterCommonUsersIds(Set<Long> setIds1, Set<Long> setIds2) {
        Set<Long> set1 = new HashSet<>(setIds1);
        Set<Long> set2 = new HashSet<>(setIds2);
        set1.retainAll(set2);
        return set1;
    }
}
