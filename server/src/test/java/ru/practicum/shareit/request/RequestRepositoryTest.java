package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RequestRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RequestRepository requestRepository;

    private Request request1;
    private Request request2;
    private Request request3;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {

        user1 = new User();
        user1.setName("User1");
        user1.setEmail("user1@mail.com");
        entityManager.persist(user1);

        user2 = new User();
        user2.setName("User2");
        user2.setEmail("user2@mail.com");
        entityManager.persist(user2);

        entityManager.flush();

        request1 = new Request();
        request1.setDescription("Need a drill");
        request1.setUserId(user1.getId());
        request1.setCreated(LocalDateTime.of(2025, 9, 17, 10, 0));
        entityManager.persist(request1);

        request2 = new Request();
        request2.setDescription("Need a hammer");
        request2.setUserId(user1.getId());
        request2.setCreated(LocalDateTime.of(2025, 9, 17, 9, 0));
        entityManager.persist(request2);

        request3 = new Request();
        request3.setDescription("Need a screwdriver");
        request3.setUserId(user2.getId());
        request3.setCreated(LocalDateTime.of(2025, 9, 17, 8, 0));
        entityManager.persist(request3);

        entityManager.flush();
    }

    @Test
    void findRequestsByUserIdOrderByCreatedDesc() {
        List<Request> requests = requestRepository.findRequestsByUserIdOrderByCreatedDesc(user1.getId());

        assertThat(requests)
                .hasSize(2)
                .containsExactly(request1, request2);
    }

    @Test
    void findRequestsByUserIdOrderByCreatedDescNonExistentUser() {
        List<Request> requests = requestRepository.findRequestsByUserIdOrderByCreatedDesc(999L);


        assertThat(requests).isEmpty();
    }

    @Test
    void findByUserIdNotOrderByCreatedDesc() {
        List<Request> requests = requestRepository.findByUserIdNotOrderByCreatedDesc(user1.getId());

        assertThat(requests)
                .hasSize(1)
                .containsExactly(request3);
    }

    @Test
    void findByUserIdNotOrderByCreatedDescAnotherId() {
        List<Request> requests = requestRepository.findByUserIdNotOrderByCreatedDesc(999L);

        assertThat(requests)
                .hasSize(3)
                .containsExactly(request1, request2, request3);
    }
}