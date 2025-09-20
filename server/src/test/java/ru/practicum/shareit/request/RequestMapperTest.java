package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestMapperTest {

    private RequestMapper requestMapper;

    @BeforeEach
    public void setUp() {
        requestMapper = new RequestMapper();
    }

    @Test
    public void testToRequestDto() {
        User user = new User();
        user.setId(2L);
        Item item = new Item(1L, "name", "description", true, user, null);
        Request request = new Request(1L, "description", LocalDateTime.now(), 1L, List.of(item));


        RequestDto result = requestMapper.toRequestDto(request);


        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("description");
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getName()).isEqualTo("name");
    }

    @Test
    public void testFromRequestDtoToRequest() {

        RequestDto requestDto = new RequestDto(1L, "description", LocalDateTime.now(), 1L, Collections.emptyList());


        Request result = requestMapper.fromRequestDtoToRequest(requestDto);


        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("description");
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getItems()).isEmpty();
    }

    @Test
    public void testItemsOfRequestToDtos() {
        User user = new User();
        user.setId(2L);
        Item item1 = new Item(1L, "Item 1", "Description 1", true, user, null);
        Item item2 = new Item(2L, "Item 2", "Description 2", false, user, null);
        List<Item> items = List.of(item1, item2);


        List<ItemDto> result = requestMapper.itemsOfRequestToDtos(items);


        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Item 1");
        assertThat(result.get(1).getName()).isEqualTo("Item 2");
    }

    @Test
    public void testItemsOfRequestToDtosEmptyList() {

        List<Item> items = Collections.emptyList();


        List<ItemDto> result = requestMapper.itemsOfRequestToDtos(items);


        assertThat(result).isEmpty();
    }
}

