package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.dto.BookingResultDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@JsonTest
public class BookingResultDtoTest {
    @Autowired
    private ObjectMapper objectMapper;

    private BookingResultDto bookingResultDto;

    @BeforeEach
    public void setUp() {
        LocalDateTime start = LocalDateTime.of(2024, 10, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 10, 10, 0);
        ItemDto itemDto = new ItemDto();
        UserDto userDto = new UserDto();

        bookingResultDto = new BookingResultDto(1L, start, end, BookingStatus.APPROVED, itemDto, userDto);
    }


    @Test
    public void testInvalidDateFormat() {
        String invalidDateString = "1265";

        assertThrows(DateTimeParseException.class, () -> {
            LocalDateTime.parse(invalidDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        });
    }
}
