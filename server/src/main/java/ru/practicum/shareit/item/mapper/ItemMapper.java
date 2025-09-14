package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.BookerDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapper {

    public ItemDto toItemDto(Item item, BookerDto lastBooking, BookerDto nextBooking, List<CommentDto> commentDtos) {
        return new ItemDto()
                .toBuilder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwner().getId())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(commentDtos)
                .requestId(item.getRequestId())
                .build();
    }

    public ItemDto toItemDto(Item item, List<CommentDto> commentDtos) {
        return new ItemDto()
                .toBuilder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwner().getId())
                .lastBooking(null)
                .nextBooking(null)
                .comments(commentDtos)
                .requestId(item.getRequestId())
                .build();
    }

    public ItemDto toItemDto(Item item) {
        return new ItemDto()
                .toBuilder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwner().getId())
                .lastBooking(null)
                .nextBooking(null)
                .comments(new ArrayList<>())
                .requestId(item.getRequestId())
                .build();

    }

    public List<ItemDto> toItemDto(List<Item> items, List<CommentDto> commentDtos) {
        return items.stream()
                .map(this::toItemDto)
                .toList();
    }

    public Item fromDtoToItem(ItemDto itemDto, UserDto userDto) {
        User user = new User()
                .toBuilder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail()).build();

        return new Item()
                .toBuilder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(user)
                .requestId(itemDto.getRequestId())
                .build();
    }
}
