package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;


import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(0L);
        user.setEmail("test@mail.com");
        user.setName("user1");

        userDto = new UserDto();
        userDto.setId(0L);
        userDto.setEmail("test@mail.com");
        userDto.setName("user1");
    }

    @Test
    void addUserSuccess() throws Exception {
        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.name").value("user1"));

        verify(userService, times(1)).createUser(any(UserDto.class));
    }

    @Test
    void testCreateUserWithInvalidEmail() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("testmail.com");
        userDto.setName("new name");

        doThrow(new ValidationException("Email is invalid"))
                .when(userService).createUser(any(UserDto.class));

        ResultActions resultActions = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        resultActions.andExpect(status().is5xxServerError());
    }

    @Test
    void testUpdateSuccessful() throws Exception {
        UserDto userToUpdate = new UserDto();
        userToUpdate.setId(1L);
        userToUpdate.setName("new user");
        userToUpdate.setEmail("test2@mail.com");

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(userService.updateUser(any(UserDto.class), anyLong())).thenReturn(userToUpdate);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("new user"))
                .andExpect(jsonPath("$.email").value("test2@mail.com"));
    }

    @Test
    void testUpdateUserWithExistingEmail() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@mail.com");
        userDto.setName("new name");

        doThrow(new ValidationException("Email exists"))
                .when(userService).updateUser(any(UserDto.class), anyLong());

        ResultActions resultActions = mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        resultActions.andExpect(status().is4xxClientError());
    }

    @Test
    void userDeleteTest() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void getUsersTest() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserByIdTest() throws Exception {
        when(userService.getUserById(0L)).thenReturn(userDto);

        mockMvc.perform(get("/users/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.name").value("user1"))
                .andExpect(jsonPath("$.email").value("test@mail.com"));

        verify(userService, times(1)).getUserById(0L);
    }

    @Test
    void userNotFoundTest() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().is2xxSuccessful());

        doThrow(new NotFoundException("Id not found for user"))
                .when(userService).getUserById(50L);
    }
}
