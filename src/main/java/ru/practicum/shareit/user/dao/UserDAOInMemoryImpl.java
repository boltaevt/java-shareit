package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.error.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDAOInMemoryImpl implements UserDAO {

    private final Map<Long, User> users = new HashMap<>();
    private long userIdTracker = 1;

    @Override
    public User createUser(User userIncoming) throws ValidationException {
        boolean exists = users.values().stream().anyMatch(user -> user.getEmail().equals(userIncoming.getEmail()));
        if (exists) {
            throw new ValidationException("Email already exists.");
        }
        User userNew = new User();
        userNew.setId(userIdTracker);
        userNew.setName(userIncoming.getName());
        userNew.setEmail(userIncoming.getEmail());
        users.put(userIdTracker, userNew);
        userIdTracker++;
        return userNew;
    }

    @Override
    public void updateUser(User user) {
        long userId = user.getId();
        if (users.containsKey(userId)) {
            users.put(userId, user);
        } else {
            throw new IllegalArgumentException("User with Id: " + userId + " does not exist. First create.");
        }
    }

    @Override
    public User getUserById(long userId) {
        return users.get(userId);
    }

    @Override
    public void deleteUserById(long userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
        } else {
            throw new IllegalArgumentException("Cannot delete nonexistent user");
        }
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean checkUserExists(long userId) {
        return users.containsKey(userId);
    }

    @Override
    public boolean checkEmailExists(String email) {
        return users.values().stream().anyMatch(user -> email.equals(user.getEmail()));
    }
}
