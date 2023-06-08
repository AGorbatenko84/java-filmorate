package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {

    List getListUsers();

    List getListIds();

    User addUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    Set getUserFriendsIdsById(Long id);

    void addFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    List getListFriends(Long id);
}
