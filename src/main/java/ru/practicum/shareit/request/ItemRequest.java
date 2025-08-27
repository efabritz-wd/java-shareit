package ru.practicum.shareit.request;

/**
 * TODO Sprint add-item-requests.
 */
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private BookingStatus status;
    @NotNull
    private User requestor;
    @NotNull
    private LocalDateTime created;
}
