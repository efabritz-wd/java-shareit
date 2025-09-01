package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface ItemService {

    List<CommentDto> getAllCommentsByItemId(Long itemId);

    CommentDto addComment(CommentDto commentDto, UserDto userDto, ItemDto itemDto);

    ItemDto getItemById(Long itemId);

    List<ItemDto> getItemsByUserId(Long userId);

    ItemDto getByItemIdAndUserId(Long itemId, Long userId);

    List<ItemDto> getAllItemsByText(String text);

    List<ItemDto> getAllItems();

    ItemDto addItem(ItemDto itemDto, UserDto userDto);

    ItemDto updateItem(ItemDto itemDto, Long itemId, UserDto userDto);

    void deleteItem(Long itemId);
}
