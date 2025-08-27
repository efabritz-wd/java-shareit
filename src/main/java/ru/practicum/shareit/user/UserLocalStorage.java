package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserLocalStorage implements UserRepository {
    private Map<Long, User> usersMap = new HashMap<>();

    public User getUserById(Long userId) {
        return usersMap.get(userId);
    }


    public List<User> getAllUsers() {
        return usersMap.values().stream().toList();
    }


    public User createUser(User user) {
        user.setId(getId());
        usersMap.put(user.getId(), user);
        return user;
    }


    public User updateUser(User user) {
        usersMap.put(user.getId(), user);
        return user;
    }


    public void deleteUser(Long userId) {
        usersMap.remove(userId);
    }

    private long getId() {
        long lastId = usersMap.values().stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }
}
