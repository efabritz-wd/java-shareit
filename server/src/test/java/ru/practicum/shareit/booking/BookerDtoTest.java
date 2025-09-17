package ru.practicum.shareit.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.item.dto.BookerDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JsonTest
public class BookerDtoTest {
    @Autowired
    private ObjectMapper objectMapper;

    private BookerDto bookerDto;

    @BeforeEach
    public void setUp() {
        LocalDateTime start = LocalDateTime.of(2025, 07, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 07, 10, 10, 0);
        bookerDto = new BookerDto(1L, start, end, 1L);
    }

    @Test
    public void testSerialize() throws Exception {
        Map<String, Object> jsonMap = new LinkedHashMap<>();
        jsonMap.put("id", 1);
        jsonMap.put("start", LocalDateTime.of(2025, 07, 1, 10, 0));
        jsonMap.put("end", LocalDateTime.of(2025, 07, 10, 10, 0));
        jsonMap.put("bookerId", 1);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(jsonMap);

        String actualJson = objectMapper.writeValueAsString(bookerDto);

        assertThat(actualJson).isEqualTo(json);

    }

    @Test
    public void testDeserialize() throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", 1L);
        jsonMap.put("start", LocalDateTime.of(2025, 07, 1, 10, 0));
        jsonMap.put("end", LocalDateTime.of(2025, 07, 10, 10, 0));
        jsonMap.put("bookerId", 1);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        BookerDto actualDto = new BookerDto((Long) jsonMap.get("id"),
                (LocalDateTime) jsonMap.get("start"), (LocalDateTime) jsonMap.get("end"), 1L);

        assertThat(actualDto.getId()).isEqualTo(1L);
        assertThat(actualDto.getStart()).isEqualTo(bookerDto.getStart());
        assertThat(actualDto.getEnd()).isEqualTo(bookerDto.getEnd());
    }

    @Test
    public void testDeserializeInvalidDate() throws JsonProcessingException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", 1);
        jsonMap.put("start", "12345");
        jsonMap.put("end", "2027-07-10T10:00:00");
        jsonMap.put("bookerId", 1);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(jsonMap);

        Assertions.assertThrows(Exception.class, () -> {
            objectMapper.readValue(json, BookerDto.class);
        });
    }
}

