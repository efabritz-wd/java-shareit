package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResultDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;


    @GetMapping("/{bookingId}")
    public BookingResultDto getBookingById(
            @PathVariable("bookingId") Long bookingId,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingByIdAndUserId(bookingId, userId);
    }

    @GetMapping
    public List<BookingResultDto> getAllBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(defaultValue = "ALL")
                                                 String state) {
        return bookingService.getBookingsByUserIdAndState(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResultDto> getAllBookingsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingsByOwnerIdAndState(userId, state);
    }

    @PostMapping
    public BookingResultDto addBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        UserDto userDto = userService.getUserById(userId);
        ItemDto itemDto = itemService.getByItemIdAndUserId(bookingRequestDto.getItemId(), userId);

        return bookingService.addBooking(itemDto, userDto, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResultDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam("approved") Boolean approved,
                                           @PathVariable("bookingId") Long bookingId) {

        return bookingService.approveBooking(userId, approved, bookingId);
    }
}
