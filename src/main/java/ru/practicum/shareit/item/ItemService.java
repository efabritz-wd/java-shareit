package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item getItemById(Long itemId);

    List<Item> getItemsByUserId(Long userId);

    List<Item> getAllItems();

    Item addItem(Item item);

    Item updateItem(Item item);

    void deleteItem(Long itemId);
}
