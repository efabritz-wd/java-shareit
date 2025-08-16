package ru.practicum.shareit.booking;

import lombok.Data;

@Data
public class BookingReview {
    private long id;
    private long bookingId;
    private long userId;
    private String text;
}
