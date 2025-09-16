package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(
        properties = "jdbc.url=jdbc:postgresql://localhost:5432/test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = ru.practicum.shareit.ShareItServer.class)
class UserServiceImplTest {

    @Autowired
    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createUserTest() {
        UserDto testUser = new UserDto();
        testUser.setName("testUser");
        testUser.setEmail("test@mail.com");

        UserDto createdUser = userService.createUser(testUser);

        assertThat(createdUser.getId()).isNotNull();
        assertThat(userRepository.findById(createdUser.getId())).isPresent();
    }

    @Test
    void updateUserTest() {
        UserDto testUser = new UserDto();
        testUser.setName("testUser1");
        testUser.setEmail("test1@mail.com");

        UserDto createdUser = userService.createUser(testUser);

        assertThat(createdUser.getId()).isNotNull();

        UserDto updateUser = new UserDto();
        updateUser.setEmail("test11@mail.com");

        UserDto updatedUser = userService.updateUser(updateUser, createdUser.getId());

        assertThat(updatedUser.getEmail()).isEqualTo("test11@mail.com");
    }

    @Test
    void getUserTest() {
        UserDto testUser = new UserDto();
        testUser.setName("testUser1");
        testUser.setEmail("test1@mail.com");

        UserDto createdUser = userService.createUser(testUser);

        assertThat(createdUser.getId()).isNotNull();
        assertThat(userRepository.findById(createdUser.getId())).isPresent();

        User userFound = userRepository.findById(createdUser.getId()).get();

        if (userFound.getId() != null) {
            assertThat(userFound.getId()).isEqualTo(createdUser.getId());
        }
    }

    @Test
    void getUsersTest() {
        UserDto testUser = new UserDto();
        testUser.setName("testUser1");
        testUser.setEmail("test1@mail.com");

        UserDto testUser2 = new UserDto();
        testUser2.setName("testUser2");
        testUser2.setEmail("test2@mail.com");

        userService.createUser(testUser);
        userService.createUser(testUser2);

        assertThat(userRepository.findAll()).isNotEmpty();
        assertThat(userRepository.findAll().size()).isEqualTo(2);
    }
}