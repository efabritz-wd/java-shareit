package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findRequestsByUserIdOrderByCreatedDesc(long userId);

    List<Request> findByUserIdNotOrderByCreatedDesc(long userId);
}
