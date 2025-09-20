package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    public void testToUserDto() {

        User user = new User(1L, " Alex", "alex@mail.com");
        UserDto result = userMapper.toUserDto(user);


        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(" Alex");
        assertThat(result.getEmail()).isEqualTo("alex@mail.com");
    }

    @Test
    public void testFromDtoToUser() {

        UserDto userDto = new UserDto(1L, "Alex", "alex@mail.com");

        User result = userMapper.fromDtoToUser(userDto);


        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Alex");
        assertThat(result.getEmail()).isEqualTo("alex@mail.com");
    }

    @Test
    public void testToUserDtoList() {

        User user1 = new User(1L, "User1", "user1@mail.com");
        User user2 = new User(2L, "User2", "user2@mail.com");
        List<User> users = List.of(user1, user2);


        List<UserDto> result = userMapper.toUserDto(users);


        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("User1");
        assertThat(result.get(1).getName()).isEqualTo("User2");
    }

    @Test
    public void testToUserDtoEmpty() {
        List<User> users = List.of();

        List<UserDto> result = userMapper.toUserDto(users);

        assertThat(result).isEmpty();
    }
}
