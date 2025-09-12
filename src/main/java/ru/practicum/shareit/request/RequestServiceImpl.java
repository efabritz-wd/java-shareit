package ru.practicum.shareit.request;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    public List<Request> getUserRequests(Long userId) {
        userRepository.findById(userId);
        return requestRepository.findRequestsByUserIdOrderByCreatedDesc(userId);
    }

    @Override
    public List<Request> getExcludingUserRequests(Long userId) {
        userRepository.findById(userId);
        return requestRepository.findByUserIdNotOrderByCreatedDesc(userId);
    }

    @Override
    public Request getRequest(Long userId, Long requestId) {
        userRepository.findById(userId);
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found for id: " + requestId));
    }

    @Override
    public Request createRequest(Request request) {
        userRepository.findById(request.userId);
        request.setCreated(LocalDateTime.now());
        return requestRepository.save(request);
    }
}
