package ru.practicum.shareit.request.dto;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    private String name;
    private String description;
    private BookingStatus status;
    private User requestor;
    private LocalDateTime created;
}

