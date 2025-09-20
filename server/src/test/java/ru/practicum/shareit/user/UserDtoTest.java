package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDtoTest {

    private ObjectMapper objectMapper;
    private Validator validator;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void serializeUserDtoToJsonTest() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("Katja")
                .email("katja@mail.com")
                .build();

        String json = objectMapper.writeValueAsString(userDto);

        assertThat(json).isEqualTo("{\"id\":1,\"name\":\"Katja\",\"email\":\"katja@mail.com\"}");
    }

    @Test
    void deserializeJsonToUserDtoTest() throws Exception {
        String json = "{\"id\":1,\"name\":\"Katja\",\"email\":\"katja@mail.com\"}";

        UserDto userDto = objectMapper.readValue(json, UserDto.class);


        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getName()).isEqualTo("Katja");
        assertThat(userDto.getEmail()).isEqualTo("katja@mail.com");
    }

    @Test
    void validateValidUserDtoTest() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("Katja")
                .email("katja@mail.com")
                .build();


        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        assertThat(violations).isEmpty();
    }

    @Test
    void validateBlankNameFailsTest() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("")
                .email("katja@mail.com")
                .build();


        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("name");
    }

    @Test
    void validateNullNameFails() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name(null)
                .email("katja@mail.com")
                .build();


        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);


        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("name");
    }

    @Test
    void validateInvalidEmailFailsTest() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("Katja")
                .email("katjaemail")
                .build();


        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("email");
    }

    @Test
    void validateBlankEmailFailsTest() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("Katja")
                .email("")
                .build();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        assertThat(violations).hasSize(1);
    }

    @Test
    void validateNullEmailFailsTest() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("Katja")
                .email(null)
                .build();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);


        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("email");
    }
}