package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.item.dto.BookerDto;
import ru.practicum.shareit.item.dto.CommentDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    Long ownerId;

    BookerDto lastBooking;
    BookerDto nextBooking;

    List<CommentDto> comments;

    @Nullable
    Long requestId;
}

