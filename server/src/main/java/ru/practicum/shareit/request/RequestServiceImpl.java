package ru.practicum.shareit.request;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Request> getUserRequests(Long userId) {
        userRepository.findById(userId);
        return requestRepository.findRequestsByUserIdOrderByCreatedDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Request> getExcludingUserRequests(Long userId) {
        userRepository.findById(userId);
        return requestRepository.findByUserIdNotOrderByCreatedDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
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
