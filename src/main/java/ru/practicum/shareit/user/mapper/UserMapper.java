package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class UserMapper {
    public UserDto toUserDto(User user) {
        return new UserDto()
                .toBuilder()
                .id(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User fromDtoToUser(UserDto userDto) {
        return new User()
                .toBuilder()
                .userId(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
