package ru.practicum.shareit.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

@Component
public class RequestMapper {
    public RequestDto toRequestDto(Request request) {
        return new RequestDto()
                .toBuilder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .userId(request.getUserId())
                .items(itemsOfRequestToDtos(request.getItems()))
                .build();
    }

    public Request fromRequestDtoToRequest(RequestDto requestDto) {
        return new Request()
                .toBuilder()
                .id(requestDto.getId())
                .description(requestDto.getDescription())
                .created(requestDto.getCreated())
                .userId(requestDto.getUserId())
                //.items(itemsDtosOfRequestToItems(requestDto.getItems()))
                .build();
    }

    public List<ItemDto> itemsOfRequestToDtos(List<Item> items) {
        return items.stream().map(item -> new ItemMapper().toItemDto(item)).toList();
    }
}
