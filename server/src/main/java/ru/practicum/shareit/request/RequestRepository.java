package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findRequestsByUserIdOrderByCreatedDesc(long userId);
    List<Request> findByUserIdNotOrderByCreatedDesc(long userId);
}
