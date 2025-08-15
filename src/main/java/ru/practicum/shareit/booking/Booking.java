package ru.practicum.shareit.booking;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @NotNull
    private Long id;
    @NotNull
    private User booker;
    @NotNull
    private Item item;
    @NotNull
    private LocalDateTime leasingStart;
    @NotNull
    private LocalDateTime leasingEnd;
    @NotNull
    private BookingStatus status;
}

