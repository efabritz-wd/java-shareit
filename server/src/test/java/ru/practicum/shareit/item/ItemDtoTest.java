package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JsonTest
public class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testValidItemDto() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "name", "desc", false, 2L,
                null, null, null, null);

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.ownerId");
    }

    @Test
    void deserializeJsonToItemDto() throws Exception {
        String json = "{\"id\":1,\"name\":\"Test item\",\"description\":\"Test description\"," +
                "\"available\":true,\"ownerId\":2,\"lastBooking\":{\"id\":1,\"bookerId\":3}," +
                "\"nextBooking\":{\"id\":2,\"bookerId\":4}," +
                "\"comments\":[{\"id\":1,\"text\":\"comment1\",\"authorName\":\"User1\",\"created\":\"2025-09-17T22:04:00\"}," +
                "{\"id\":2,\"text\":\"comment2\",\"authorName\":\"user2\",\"created\":\"2025-09-17T23:04:00\"}]," +
                "\"requestId\":5}";

        ItemDto itemDto = objectMapper.readValue(json, ItemDto.class);

        assertThat(itemDto.getId()).isEqualTo(1L);
        assertThat(itemDto.getName()).isEqualTo("Test item");
        assertThat(itemDto.getDescription()).isEqualTo("Test description");
        assertThat(itemDto.getAvailable()).isTrue();
        assertThat(itemDto.getOwnerId()).isEqualTo(2L);
        assertThat(itemDto.getLastBooking()).isNotNull();
        assertThat(itemDto.getLastBooking().getId()).isEqualTo(1L);
        assertThat(itemDto.getLastBooking().getBookerId()).isEqualTo(3L);
        assertThat(itemDto.getNextBooking()).isNotNull();
        assertThat(itemDto.getNextBooking().getId()).isEqualTo(2L);
        assertThat(itemDto.getNextBooking().getBookerId()).isEqualTo(4L);
        assertThat(itemDto.getComments()).hasSize(2);
        assertThat(itemDto.getComments().get(0).getId()).isEqualTo(1L);
        assertThat(itemDto.getComments().get(0).getText()).isEqualTo("comment1");
        assertThat(itemDto.getComments().get(0).getAuthorName()).isEqualTo("User1");
        assertThat(itemDto.getComments().get(0).getCreated()).isEqualTo(LocalDateTime.of(2025, 9, 17, 22, 4));
        assertThat(itemDto.getComments().get(1).getId()).isEqualTo(2L);
        assertThat(itemDto.getComments().get(1).getText()).isEqualTo("comment2");
        assertThat(itemDto.getComments().get(1).getAuthorName()).isEqualTo("user2");
        assertThat(itemDto.getComments().get(1).getCreated()).isEqualTo(LocalDateTime.of(2025, 9, 17, 23, 4));
        assertThat(itemDto.getRequestId()).isEqualTo(5L);
    }

    @Test
    void deserializeInvalidJsonThrowsException() {
        String invalidJson = "{\"id\":1,\"name\":\"Test\",\"description\":\"Test desc\"," +
                "\"available\":\"invalid\",\"ownerId\":2}";

        assertThrows(InvalidFormatException.class, () -> objectMapper.readValue(invalidJson, ItemDto.class));
    }
}

