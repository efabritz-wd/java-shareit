package ru.practicum.shareit.item.dto;

import lombok.Value;
import java.time.LocalDateTime;

@Value
public class BookerDto {
    Long id;

    LocalDateTime start;

    LocalDateTime end;

    Long bookerId;
}
