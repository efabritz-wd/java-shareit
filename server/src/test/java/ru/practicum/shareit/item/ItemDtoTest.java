package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;


import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

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
}
