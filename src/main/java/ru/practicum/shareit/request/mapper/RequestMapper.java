package ru.practicum.shareit.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.dto.RequestDto;

@Component
public class RequestMapper {
    public RequestDto toRequestDto(Request request) {
        return new RequestDto()
                .toBuilder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .userId(request.getUserId())
                .items(request.getItems())
                .build();
    }

    public Request fromRequestDtoToRequest(RequestDto requestDto) {
        return new Request()
                .toBuilder()
                .id(requestDto.getId())
                .description(requestDto.getDescription())
                .created(requestDto.getCreated())
                .userId(requestDto.getUserId())
                .items(requestDto.getItems())
                .build();
    }
}
