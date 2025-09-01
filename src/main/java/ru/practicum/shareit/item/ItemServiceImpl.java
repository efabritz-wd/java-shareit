package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.BookerDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;


    @Override
    public List<CommentDto> getAllCommentsByItemId(Long itemId) {
        List<Comment> comments = commentRepository.findAllByItemId(itemId);

        return comments
                .stream()
                .map(commentMapper::toCommentDto)
                .toList();

    }

    @Override
    public CommentDto addComment(CommentDto commentDto, UserDto userDto, ItemDto itemDto) {
        Comment comment = commentMapper.toComment(commentDto, userDto, itemDto);
        List<Booking> bookings = bookingRepository.findCompletedBookingsByUser(userDto.getId(), itemDto.getId(), LocalDateTime.now());
        log.error("Comments for booking not found");
        System.out.println("\"Comments for booking not found\"");
        if (bookings.isEmpty()) {
            throw new IllegalArgumentException("User did not rent this item.");
        }

        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    public BookerDto getPreviousBooking(Item item) {
        Booking booking = bookingRepository.getLastBooking(item.getId(), LocalDateTime.now());
        if (booking == null) {
            return null;
        }
        return bookingMapper.toBookerDto(booking);
    }

    public BookerDto getNextBooking(Item item) {
        Booking booking = bookingRepository.getNextBooking(item.getId(), LocalDateTime.now());
        if (booking == null) {
            return null;
        }
        return bookingMapper.toBookerDto(booking);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new NotFoundException("Item not found");
        }
        Item item = itemRepository.findById(itemId).get();
        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsByUserId(Long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        return items.stream().map(itemMapper::toItemDto).toList();
    }

    @Override
    public ItemDto getByItemIdAndUserId(Long itemId, Long userId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new NotFoundException("Item not found for id: " + itemId);
        }
        Item item = itemRepository.findById(itemId).get();

        if (userId.equals(item.getOwner().getId())) {
            return itemMapper.toItemDto(item, getPreviousBooking(item), getNextBooking(item), getAllCommentsByItemId(itemId));
        }

        return itemMapper.toItemDto(item, getAllCommentsByItemId(itemId));
    }

    @Override
    public List<ItemDto> getAllItemsByText(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        List<Item> items = itemRepository.searchByText(text);

        return items.stream()
                .map(item -> itemMapper.toItemDto(item, getPreviousBooking(item), getNextBooking(item), getAllCommentsByItemId(item.getId())))
                .toList();
    }

    @Override
    public List<ItemDto> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return items.stream().map(itemMapper::toItemDto).toList();
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, UserDto userDto) {
        Item item = itemMapper.fromDtoToItem(itemDto, userDto);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, UserDto userDto) {
        Item item = itemRepository.findByIdAndOwnerId(itemId, userDto.getId());

        if (item == null) {
            throw new NotFoundException("Update. Item not found");
        }
        Item itemToUpdate = itemMapper.fromDtoToItem(itemDto, userDto);

        item.setName(Objects.requireNonNullElse(itemToUpdate.getName(), item.getName()));

        item.setDescription(Objects.requireNonNullElse(itemToUpdate.getDescription(), item.getDescription()));

        item.setAvailable(Objects.requireNonNullElse(itemToUpdate.getAvailable(), item.getAvailable()));

        return itemMapper.toItemDto(item);
    }

    @Override
    public void deleteItem(Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);

        if (item.isEmpty()) {
            throw new NotFoundException("Delete. Item not found");
        }
        itemRepository.deleteById(itemId);
    }
}
