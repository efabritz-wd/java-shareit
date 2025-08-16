package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    User getUserById(Long id);

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long userId);
}
