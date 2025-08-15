package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class UserLocalStorage implements UserRepository {
    private Map<Long, User> usersMap = new HashMap<>();

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean isValidEmail(User user) {
        if (user.getEmail() == null) throw new NotFoundException("Email was not found");
        Matcher matcher = EMAIL_PATTERN.matcher(user.getEmail());
        return matcher.matches();
    }

    public User getUserById(Long userId) {
        if (userId == null || !usersMap.containsKey(userId)) {
            throw new NotFoundException("User not found");
        }
        return usersMap.get(userId);
    }


    public List<User> getAllUsers() {
        return usersMap.values().stream().toList();
    }


    public User createUser(User user) {
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        if (checkDoubleEmail(user)) {
            throw new ConditionsNotMetException("User with this email exists already");
        }

        if (!isValidEmail(user)) {
            throw new ValidationException("Email is invalid");
        }

        user.setUserId(getId());
        usersMap.put(user.getUserId(), user);
        return user;
    }


    public User updateUser(User user) {
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        if (!usersMap.containsKey(user.getUserId())) {
            throw new NotFoundException("User not found in a map");
        }

        if (checkDoubleEmail(user)) {
            throw new ConditionsNotMetException("User with this email exists already");
        }

        usersMap.put(user.getUserId(), user);
        return user;
    }


    public void deleteUser(Long userId) {
        if (userId == null) {
            throw new NotFoundException("User not found");
        }

        if (!usersMap.containsKey(userId)) {
            throw new NotFoundException("User not found in a map");
        }

        usersMap.remove(userId);
    }

    private long getId() {
        long lastId = usersMap.values().stream()
                .mapToLong(User::getUserId)
                .max()
                .orElse(0);
        return lastId + 1;
    }

    private boolean checkDoubleEmail(User user) {
        String email = user.getEmail();
        Long userId = user.getUserId();
        Optional<User> userExists = usersMap.values().stream()
                .filter(userCheck -> !userCheck.getUserId().equals(userId) && userCheck.getEmail().equals(email))
                .findFirst();
        return userExists.isPresent();
    }
}
