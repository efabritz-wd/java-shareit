package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable("userId") Long userId) {
        User user = userService.getUserById(userId);
        return userMapper.toUserDto(user);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users
                .stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        User user = userMapper.fromDtoToUser(userDto);
        User userCreated = userService.createUser(user);
        return userMapper.toUserDto(userCreated);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable("userId") Long userId,
                              @RequestBody UserDto userDto) {
        userDto.setId(userId);
        User user = userMapper.fromDtoToUser(userDto);
        User userUpdated = userService.updateUser(user);
        return userMapper.toUserDto(userUpdated);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
    }
}
