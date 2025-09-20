package ru.practicum.shareit.request;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/requests")
public class RequestController {
    private final UserService userService;
    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @GetMapping("/{requestId}")
    public RequestDto getRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable("requestId") Long requestId) {
        Request request = requestService.getRequest(userId, requestId);
        return requestMapper.toRequestDto(request);
    }

    @GetMapping
    public List<RequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<Request> requests = requestService.getUserRequests(userId);
        return requests.stream().map(request -> requestMapper.toRequestDto(request)).toList();
    }

    @PostMapping
    public RequestDto createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestBody RequestDto requestDto) {
        userService.getUserById(userId);
        requestDto.setUserId(userId);
        Request fromJsonRequest = requestMapper.fromRequestDtoToRequest(requestDto);
        Request request = requestService.createRequest(fromJsonRequest);

        return requestMapper.toRequestDto(request);
    }

    @GetMapping("/all")
    public List<RequestDto> getOthersRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<Request> requests = requestService.getExcludingUserRequests(userId);
        return requests.stream().map(request -> requestMapper.toRequestDto(request)).toList();
    }
}
