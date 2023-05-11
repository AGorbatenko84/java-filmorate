package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {

    List getListUsers();

    List getListIds();

    void addUser(User user);

    void updateUser(User user);

    User getUserById(Long id);

    Set getUserFriendsIdsById(Long id);
}
