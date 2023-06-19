package ru.yandex.practicum.filmorate.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Qualifier("memory")
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users;
    private long idUser;

    public InMemoryUserStorage() {
        this.users = new HashMap<>();
        this.idUser = 0;
    }

    @Override
    public List<User> getListUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public List<Long> getListIds() {
        return new ArrayList<>(users.keySet());
    }

    @Override
    public User addUser(User user) {
        this.idUser++;
        user.setId(this.idUser);
        users.put(this.idUser, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Set<Long> getUserFriendsIdsById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id).getFriends();
        }
        return null;
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        users.get(id).getFriends().add(friendId);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        users.get(id).getFriends().remove(friendId);
    }

    @Override
    public List<User> getListFriends(Long id) {
        Set<Long> idsFriends = getUserFriendsIdsById(id);
        List<User> friends = new ArrayList<>();
        for (Long idFriend : idsFriends) {
            friends.add(getUserById(idFriend));
        }
        return friends;
    }
}


