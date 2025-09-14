package ru.practicum.shareit.request;

import java.util.List;

public interface RequestService {
    List<Request> getUserRequests(Long userId);

    List<Request> getExcludingUserRequests(Long userId);

    Request getRequest(Long userId, Long requestId);

    Request createRequest(Request request);
}
