package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResultDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface BookingService {
    BookingResultDto addBooking(ItemDto itemDto, UserDto userDto, BookingRequestDto bookingRequestDto);

    BookingResultDto approveBooking(Long userId, Boolean approved, Long bookingId);

    BookingResultDto getBookingByIdAndUserId(Long bookingId, Long userId);

    List<BookingResultDto> getBookingsByUserIdAndState(Long userId, String state);

    List<BookingResultDto> getBookingsByOwnerIdAndState(Long userId, String state);

}
