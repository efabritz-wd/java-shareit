package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class CommentDto {

    Long id;

    @NotBlank
    String text;

    String authorName;

    LocalDateTime created;
}