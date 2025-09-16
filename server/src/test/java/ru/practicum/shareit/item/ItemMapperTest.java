package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.BookerDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemMapperTest {

    private ItemMapper itemMapper;

    @BeforeEach
    public void setUp() {
        itemMapper = new ItemMapper();
    }

    @Test
    public void testToItemDtoWithBookingsAndComments() {
        User owner = new User(1L, "Owner Name", "owner@example.com");
        Item item = new Item(1L, "Item Name", "Item Description", true, owner, null);
        BookerDto lastBooking = new BookerDto(1L, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(10), 4L);
        BookerDto nextBooking = new BookerDto(2L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), 5L);
        List<CommentDto> comments = List.of(new CommentDto(1L, "Comment", "User", null));

        ItemDto result = itemMapper.toItemDto(item, lastBooking, nextBooking, comments);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Item Name");
        assertThat(result.getDescription()).isEqualTo("Item Description");
        assertThat(result.getAvailable()).isTrue();
        assertThat(result.getOwnerId()).isEqualTo(1L);
        assertThat(result.getLastBooking()).isEqualTo(lastBooking);
        assertThat(result.getNextBooking()).isEqualTo(nextBooking);
        assertThat(result.getComments()).isEqualTo(comments);
    }

    @Test
    public void testToItemDtoWithComments() {
        User owner = new User(1L, "Owner Name", "owner@example.com");
        Item item = new Item(1L, "Item Name", "Item Description", true, owner, null);
        List<CommentDto> comments = List.of(new CommentDto(1L, "Comment", "User", null));

        ItemDto result = itemMapper.toItemDto(item, comments);

        assertThat(result.getLastBooking()).isNull();
        assertThat(result.getNextBooking()).isNull();
        assertThat(result.getComments()).isEqualTo(comments);
    }

    @Test
    public void testToItemDtoWithoutComments() {
        User owner = new User(1L, "user Name", "user@mail.com");
        Item item = new Item(1L, "Item Name", "Item Description", true, owner, null);

        ItemDto result = itemMapper.toItemDto(item);

        assertThat(result.getComments()).isEmpty();
    }

    @Test
    public void testFromDtoToItem() {
        UserDto userDto = new UserDto(1L, "User Name", "user@example.com");
        ItemDto itemDto = new ItemDto(1L, "Item Name", "Item Description",
                true, 1L, null, null, null, 2L);

        Item result = itemMapper.fromDtoToItem(itemDto, userDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Item Name");
        assertThat(result.getDescription()).isEqualTo("Item Description");
        assertThat(result.getAvailable()).isTrue();
        assertThat(result.getOwner().getId()).isEqualTo(1L);
    }

    @Test
    public void testToItemDtoList() {
        User owner = new User(1L, "username", "user@mail.com");
        Item item1 = new Item(1L, "Item 1", "Description 1", true, owner, null);
        Item item2 = new Item(2L, "Item 2", "Description 2", false, owner, null);
        List<Item> items = List.of(item1, item2);
        List<CommentDto> comments = List.of();

        List<ItemDto> result = itemMapper.toItemDto(items, comments);


        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Item 1");
        assertThat(result.get(1).getName()).isEqualTo("Item 2");
    }
}

