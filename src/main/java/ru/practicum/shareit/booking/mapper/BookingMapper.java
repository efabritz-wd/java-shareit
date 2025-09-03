package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResultDto;
import ru.practicum.shareit.item.dto.BookerDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapper {

    public Booking requestToBooking(BookingRequestDto bookingRequestDto,
                                    ItemDto itemDto, UserDto userDto, BookingStatus status) {
        User owner = new User();
        owner.setId(itemDto.getOwnerId());

        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);

        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        Booking booking = new Booking();
        booking.setStart(bookingRequestDto.getStart());
        booking.setEnd(bookingRequestDto.getEnd());
        booking.setStatus(status);
        booking.setBooker(user);
        booking.setItem(item);

        return booking;
    }

    public BookingResultDto fromBookingToDto(Booking booking) {
        ItemDto itemDto = new ItemDto(booking.getItem().getId(),
                booking.getItem().getName(),
                booking.getItem().getDescription(),
                booking.getItem().getAvailable(),
                booking.getItem().getOwner().getId(),
                null,
                null,
                new ArrayList<>());

        UserDto userDto = new UserDto(booking.getBooker().getId(),
                booking.getBooker().getName(),
                booking.getBooker().getEmail());

        BookingResultDto brdto = new BookingResultDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                itemDto,

                userDto);
        return brdto;
    }

    public List<BookingResultDto> fromBookingToDto(List<Booking> bookings) {
        return bookings.stream()
                .map(this::fromBookingToDto)
                .toList();
    }

    public BookerDto toBookerDto(Booking booking) {
        return new BookerDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getBooker().getId()
        );
    }
}


