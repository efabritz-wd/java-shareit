package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO Sprint add-controllers.
 */
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final UserService userService;

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable("itemId") Long itemId) {
        User owner = userService.getUserById(userId);
        Item item = itemService.getItemById(itemId);
        if (!owner.getUserId().equals(item.getOwner().getUserId())) {
            throw new RuntimeException("User id does not correspond owner id");
        }
        return itemMapper.toItemDto(item);
    }

    @GetMapping
    public List<ItemDto> getItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<Item> items = itemService.getItemsByUserId(userId);
        return items.stream()
                .map(itemMapper::toItemDto)
                .toList();
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam String text) {
        userService.getUserById(userId);

        if (text.isBlank()) {
            return List.of();
        }

        return itemService.getAllItems().stream()
                .filter(Item::getAvailable)
                .filter(item -> text.toLowerCase().contains(item.getName().toLowerCase())
                        || text.toLowerCase().contains(item.getDescription().toLowerCase()))
                .map(itemMapper::toItemDto)
                .toList();
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @Valid @RequestBody ItemDto itemDto) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("User for item owner was not found");
        }
        Item item = itemMapper.fromDtoToItem(itemDto);
        item.setOwner(user);
        Item newItem = itemService.addItem(item);
        return itemMapper.toItemDto(newItem);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody ItemDto itemDto,
                              @PathVariable("itemId") Long itemId) {
        User user = userService.getUserById(userId);
        Item item = itemService.getItemById(itemId);
        Item itemBuilt = itemMapper.fromDtoToItem(itemDto);
        itemBuilt.setItemId(item.getItemId());
        itemBuilt.setOwner(user);
        Item itemUpdated = itemService.updateItem(itemBuilt);

        return itemMapper.toItemDto(itemUpdated);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(Long itemId) {
        userService.deleteUser(itemId);
    }
}

