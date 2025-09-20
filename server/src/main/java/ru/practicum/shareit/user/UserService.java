package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();

    UserDto createUser(UserDto user);

    UserDto updateUser(UserDto user, Long userId);

    void deleteUser(Long userId);
}
