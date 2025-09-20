package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private Item item;
    private User owner;
    private User commenter;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@mail.com");
        owner = userRepository.save(owner);


        commenter = new User();
        commenter.setName("Commenter");
        commenter.setEmail("commenter@mail.com");
        commenter = userRepository.save(commenter);

        item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(owner);
        item = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(commenter);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);
    }

    @Test
    void findAllByItemIdWithCommentsReturnsList() {
        Comment comment1 = new Comment();
        comment1.setText("Great item!");
        comment1.setItem(item);
        comment1.setAuthor(commenter);
        comment1.setCreated(LocalDateTime.now());
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setText("Very useful!");
        comment2.setItem(item);
        comment2.setAuthor(commenter);
        comment2.setCreated(LocalDateTime.now());
        commentRepository.save(comment2);

        List<Comment> comments = commentRepository.findAllByItemId(item.getId());

        assertThat(comments).hasSize(2);
        assertThat(comments).extracting(Comment::getText).containsExactlyInAnyOrder("Great item!", "Very useful!");
        assertThat(comments).extracting(Comment::getItem).allMatch(item -> item.getId().equals(item.getId()));
        assertThat(comments).extracting(Comment::getAuthor).allMatch(user -> user.getId().equals(commenter.getId()));
    }

    @Test
    void findAllByItemIdWithNoCommentsReturnsEmptyList() {
        List<Comment> comments = commentRepository.findAllByItemId(item.getId());

        assertThat(comments).isEmpty();
    }

    @Test
    void findAllByItemIdWithNonExistentItemIdReturnsEmptyList() {
        List<Comment> comments = commentRepository.findAllByItemId(999L);

        assertThat(comments).isEmpty();
    }
}