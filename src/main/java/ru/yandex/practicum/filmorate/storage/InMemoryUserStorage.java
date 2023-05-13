package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

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
    public void addUser(User user) {
        this.idUser++;
        user.setId(this.idUser);
        users.put(this.idUser, user);
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
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
}


