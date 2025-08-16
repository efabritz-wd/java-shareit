package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getUserById(Long id) {
        if (userRepository.getUserById(id) == null) {
            throw new NotFoundException("User not found");
        }
        return userRepository.getUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User createUser(User user) {
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        if (checkDoubleEmail(user)) {
            throw new ConditionsNotMetException("User with this email exists already");
        }
        return userRepository.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        if (userRepository.getUserById(user.getId()) == null) {
            throw new NotFoundException("User not found in a map");
        }

        if (checkDoubleEmail(user)) {
            throw new ConditionsNotMetException("User with this email exists already");
        }
        return userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.getUserById(userId);

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        if (userRepository.getUserById(user.getId()) == null) {
            throw new NotFoundException("User not found in a map");
        }

        userRepository.deleteUser(userId);
    }

    private boolean checkDoubleEmail(User user) {
        String email = user.getEmail();
        Long userId = user.getId();
        List<User> usersList = userRepository.getAllUsers();
        Optional<User> userExists = usersList.stream()
                .filter(userCheck -> !userCheck.getId().equals(userId) && userCheck.getEmail().equals(email))
                .findFirst();
        return userExists.isPresent();
    }
}
