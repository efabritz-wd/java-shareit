package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.RequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class RequestDtoTest {
    @Autowired
    private JacksonTester<RequestDto> json;

    @Test
    public void testValidCommentDto() throws Exception {
        RequestDto requestDto = new RequestDto(1L, "text desc", LocalDateTime.now().minusHours(2),
                2L, List.of(new ItemDto()));

        JsonContent<RequestDto> result = json.write(requestDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.created");
        assertThat(result).hasJsonPath("$.userId");
        assertThat(result).hasJsonPath("$.items");
    }
}
