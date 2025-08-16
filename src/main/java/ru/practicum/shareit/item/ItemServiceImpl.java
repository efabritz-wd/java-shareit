package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public Item getItemById(Long itemId) {
        if (itemId == null || itemRepository.getItemById(itemId) == null) {
            throw new NotFoundException("Item not found");
        }
        return itemRepository.getItemById(itemId);
    }

    @Override
    public List<Item> getItemsByUserId(Long userId) {
        return itemRepository.getItemsByUserId(userId);
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.getAllItems();
    }

    @Override
    public Item addItem(Item item) {
        if (item == null) {
            throw new NotFoundException("Item not found");
        }

        return itemRepository.addItem(item);
    }

    @Override
    public Item updateItem(Item item) {
        if (item == null) {
            throw new NotFoundException("Item not found");
        }

        if (itemRepository.getItemById(item.getId()) == null) {
            throw new NotFoundException("Item not found in a map");
        }

        return itemRepository.updateItem(item);
    }

    @Override
    public void deleteItem(Long itemId) {
        Item item = getItemById(itemId);

        if (item == null) {
            throw new NotFoundException("Item not found");
        }

        if (itemRepository.getItemById(item.getId()) == null) {
            throw new NotFoundException("Item not found in a map");
        }
        itemRepository.deleteItem(itemId);
    }
}
