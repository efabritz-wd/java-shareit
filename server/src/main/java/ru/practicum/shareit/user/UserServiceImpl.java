package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto getUserById(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new NotFoundException("User not found");
        }
        User user = userRepository.findById(id).get();
        return userMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toUserDto(users);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.fromDtoToUser(userDto);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        User userToUpdate = userMapper.fromDtoToUser(getUserById(userId));
        User userWithUpdateParams = userMapper.fromDtoToUser(userDto);

        userToUpdate.setName(Objects.requireNonNullElse(userWithUpdateParams.getName(), userToUpdate.getName()));
        userToUpdate.setEmail(Objects.requireNonNullElse(userWithUpdateParams.getEmail(), userToUpdate.getEmail()));

        return userMapper.toUserDto(userRepository.save(userToUpdate));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}