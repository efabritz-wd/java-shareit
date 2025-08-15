package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

@Component
public class BookingMapper {
    private LocalDateTime leasingStart;
    private LocalDateTime leasingEnd;
    private BookingStatus status;
    public static BookingDto toItemDto(Booking booking) {
        return new BookingDto(
                booking.getLeasingStart(),
                booking.getLeasingEnd(),
                booking.getStatus()
        );
    }
}


