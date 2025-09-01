package ru.practicum.shareit.user;
/*
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImplLocal implements UserService {
    private final UserRepositoryLocal userRepositoryLocal;

    @Override
    public UserDto getUserById(Long id) {
        if (userRepositoryLocal.getUserById(id) == null) {
            throw new NotFoundException("User not found");
        }
        return userRepositoryLocal.getUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepositoryLocal.getAllUsers();
    }

    @Override
    public User createUser(User user) {
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        if (checkDoubleEmail(user)) {
            throw new ConditionsNotMetException("User with this email exists already");
        }
        return userRepositoryLocal.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        if (userRepositoryLocal.getUserById(user.getId()) == null) {
            throw new NotFoundException("User not found in a map");
        }

        if (checkDoubleEmail(user)) {
            throw new ConditionsNotMetException("User with this email exists already");
        }
        return userRepositoryLocal.updateUser(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepositoryLocal.getUserById(userId);

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        if (userRepositoryLocal.getUserById(user.getId()) == null) {
            throw new NotFoundException("User not found in a map");
        }

        userRepositoryLocal.deleteUser(userId);
    }

    private boolean checkDoubleEmail(User user) {
        String email = user.getEmail();
        Long userId = user.getId();
        List<User> usersList = userRepositoryLocal.getAllUsers();
        Optional<User> userExists = usersList.stream()
                .filter(userCheck -> !userCheck.getId().equals(userId) && userCheck.getEmail().equals(email))
                .findFirst();
        return userExists.isPresent();
    }
}
*/