package ru.practicum.shareit.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Component
public class RequestMapper {
    public static ItemRequestDto toItemDto(ItemRequest request) {
        return new ItemRequestDto(
                request.getName(),
                request.getDescription(),
                request.getStatus(),
                request.getRequestor(),
                request.getCreated()
        );
    }
}
