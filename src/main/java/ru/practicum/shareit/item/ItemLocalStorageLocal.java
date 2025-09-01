package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ItemLocalStorageLocal implements ItemRepositoryLocal {
    private Map<Long, Item> itemsMap = new HashMap<>();

    public Item getItemById(Long itemId) {
        return itemsMap.get(itemId);
    }


    public List<Item> getItemsByUserId(Long userId) {
        return itemsMap.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .toList();
    }


    public List<Item> getAllItems() {
        return itemsMap.values().stream().toList();
    }


    public Item addItem(Item item) {
        item.setId(getId());
        itemsMap.put(item.getId(), item);
        return item;
    }


    public Item updateItem(Item item) {
        Item existingItem = itemsMap.get(item.getId());

        if (item.getName() != null) existingItem.setName(item.getName());
        if (item.getDescription() != null) existingItem.setDescription(item.getDescription());
        if (item.getAvailable() != null) existingItem.setAvailable(item.getAvailable());

        itemsMap.put(existingItem.getId(), existingItem);
        return existingItem;
    }

    public void deleteItem(Long itemId) {
        itemsMap.remove(itemId);
    }

    private long getId() {
        long lastId = itemsMap.values().stream()
                .mapToLong(Item::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }
}
