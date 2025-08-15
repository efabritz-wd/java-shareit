package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ItemLocalStorage implements ItemRepository {
    private Map<Long, Item> itemsMap = new HashMap<>();

    public Item getItemById(Long itemId) {
        if (itemId == null || !itemsMap.containsKey(itemId)) {
            throw new NotFoundException("Item not found");
        }
        return itemsMap.get(itemId);
    }


    public List<Item> getItemsByUserId(Long userId) {
        return itemsMap.values().stream()
                .filter(item -> item.getOwner().getUserId().equals(userId))
                .toList();
    }


    public List<Item> getAllItems() {
        return itemsMap.values().stream().toList();
    }


    public Item addItem(Item item) {
        if (item == null) {
            throw new NotFoundException("Item not found");
        }

        item.setItemId(getId());
        itemsMap.put(item.getItemId(), item);
        return item;
    }


    public Item updateItem(Item item) {
        if (item == null) {
            throw new NotFoundException("Item not found");
        }

        if (!itemsMap.containsKey(item.getItemId())) {
            throw new NotFoundException("Item not found in a map");
        }

        Item existingItem = itemsMap.get(item.getItemId());

        if (item.getName() != null) existingItem.setName(item.getName());
        if (item.getDescription() != null) existingItem.setDescription(item.getDescription());
        if (item.getAvailable() != null) existingItem.setAvailable(item.getAvailable());

        itemsMap.put(existingItem.getItemId(), existingItem);
        return existingItem;
    }


    public void deleteItem(Long itemId) {
        if (itemId == null) {
            throw new NotFoundException("Item not found");
        }

        if (!itemsMap.containsKey(itemId)) {
            throw new NotFoundException("Item not found in a map");
        }

        itemsMap.remove(itemId);
    }

    private long getId() {
        long lastId = itemsMap.values().stream()
                .mapToLong(Item::getItemId)
                .max()
                .orElse(0);
        return lastId + 1;
    }
}
