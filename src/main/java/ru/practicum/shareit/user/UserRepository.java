package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {
    User getUserById(Long id);

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long userId);
}
