package ru.practicum.shareit.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JsonTest
public class BookingRequestDtoTest {
    @Autowired
    private ObjectMapper objectMapper;

    private BookingRequestDto bookingRequestDto;

    @BeforeEach
    public void setUp() {
        LocalDateTime start = LocalDateTime.of(2025, 07, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 07, 10, 10, 0);
        bookingRequestDto = new BookingRequestDto(1L, start, end);
    }

    @Test
    public void testSerialize() throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("itemId", 1);
        jsonMap.put("start", LocalDateTime.of(2025, 07, 1, 10, 0));
        jsonMap.put("end", LocalDateTime.of(2025, 07, 10, 10, 0));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(jsonMap);

        String actualJson = objectMapper.writeValueAsString(bookingRequestDto);

        assertThat(actualJson).isEqualTo(json);

    }

    @Test
    public void testDeserialize() throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("itemId", 1L);
        jsonMap.put("start", LocalDateTime.of(2025, 07, 1, 10, 0));
        jsonMap.put("end", LocalDateTime.of(2025, 07, 10, 10, 0));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        BookingRequestDto actualDto = new BookingRequestDto((Long) jsonMap.get("itemId"),
                (LocalDateTime) jsonMap.get("start"), (LocalDateTime) jsonMap.get("end"));

        assertThat(actualDto.getItemId()).isEqualTo(1L);
        assertThat(actualDto.getStart()).isEqualTo(bookingRequestDto.getStart());
        assertThat(actualDto.getEnd()).isEqualTo(bookingRequestDto.getEnd());
    }

    @Test
    public void testDeserializeInvalidDate() throws JsonProcessingException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("itemId", 1);
        jsonMap.put("start", "12345");
        jsonMap.put("end", "2027-07-10T10:00:00");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(jsonMap);

        Assertions.assertThrows(Exception.class, () -> {
            objectMapper.readValue(json, BookingRequestDto.class);
        });
    }
}
