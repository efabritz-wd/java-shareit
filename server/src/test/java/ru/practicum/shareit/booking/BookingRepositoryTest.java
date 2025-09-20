package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepository;

    private User user1;
    private User user2;
    private Item item1;
    private Item item2;
    private Booking booking1;
    private Booking booking2;
    private Booking booking3;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.of(2025, 9, 17, 13, 30);

        user1 = new User();
        user1.setName("User1");
        user1.setEmail("user1@mail.com");
        entityManager.persist(user1);

        user2 = new User();
        user2.setName("User2");
        user2.setEmail("user2@mail.com");
        entityManager.persist(user2);

        item1 = new Item();
        item1.setName("Item1");
        item1.setDescription("Desc Item1");
        item1.setAvailable(true);
        item1.setOwner(user1);
        entityManager.persist(item1);

        item2 = new Item();
        item2.setName("Item2");
        item2.setDescription("Desc Item2");
        item2.setAvailable(true);
        item2.setOwner(user2);
        entityManager.persist(item2);

        // Create bookings
        booking1 = new Booking();
        booking1.setBooker(user1);
        booking1.setItem(item1);
        booking1.setStart(now.minusDays(2));
        booking1.setEnd(now.minusDays(1));
        booking1.setStatus(BookingStatus.APPROVED);
        entityManager.persist(booking1);

        booking2 = new Booking();
        booking2.setBooker(user1);
        booking2.setItem(item1);
        booking2.setStart(now.minusDays(1));
        booking2.setEnd(now.plusDays(1));
        booking2.setStatus(BookingStatus.APPROVED);
        entityManager.persist(booking2);

        booking3 = new Booking();
        booking3.setBooker(user2);
        booking3.setItem(item2);
        booking3.setStart(now.plusDays(1));
        booking3.setEnd(now.plusDays(2));
        booking3.setStatus(BookingStatus.WAITING);
        entityManager.persist(booking3);

        entityManager.flush();
    }

    @Test
    void findByBookerId() {

        List<Booking> bookings = bookingRepository.findByBookerId(user1.getId());


        assertThat(bookings)
                .hasSize(2)
                .containsExactly(booking2, booking1);
    }

    @Test
    void findByBookerIdAndStatus() {

        List<Booking> bookings = bookingRepository.findByBookerIdAndStatus(
                user1.getId(), BookingStatus.APPROVED, Sort.by(Sort.Direction.DESC, "start"));


        assertThat(bookings)
                .hasSize(2)
                .containsExactly(booking2, booking1);
    }

    @Test
    void findByBookerIdAndStartDateIsBeforeAndEndDateIsAfterCurrentBookings() {

        List<Booking> bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(
                user1.getId(), now, now, Sort.by(Sort.Direction.DESC, "start"));


        assertThat(bookings)
                .hasSize(1)
                .containsExactly(booking2);
    }

    @Test
    void findByBookerIdAndEndDateIsBeforePastBookings() {

        List<Booking> bookings = bookingRepository.findByBookerIdAndEndIsBefore(
                user1.getId(), now, Sort.by(Sort.Direction.DESC, "start"));


        assertThat(bookings)
                .hasSize(1)
                .containsExactly(booking1);
    }

    @Test
    void findByBookerIdAndStartDateIsAfterFutureBookings() {

        List<Booking> bookings = bookingRepository.findByBookerIdAndStartIsAfter(
                user2.getId(), now, Sort.by(Sort.Direction.DESC, "start"));


        assertThat(bookings)
                .hasSize(1)
                .containsExactly(booking3);
    }

    @Test
    void findByItemIdIn_shouldReturnBookingsForItemIds() {

        List<Booking> bookings = bookingRepository.findByItemIdIn(
                List.of(item1.getId()), Sort.by(Sort.Direction.DESC, "start"));


        assertThat(bookings)
                .hasSize(2)
                .containsExactly(booking2, booking1);
    }

    @Test
    void findByItemIdInAndStatusBookingsForItemIdsAndStatus() {

        List<Booking> bookings = bookingRepository.findByItemIdInAndStatus(
                List.of(item1.getId()), BookingStatus.APPROVED, Sort.by(Sort.Direction.DESC, "start"));


        assertThat(bookings)
                .hasSize(2)
                .containsExactly(booking2, booking1);
    }

    @Test
    void findByItemIdInAndStartDateIsBeforeAndEndDateIsAfterCurrentBookingsForItemIds() {

        List<Booking> bookings = bookingRepository.findByItemIdInAndStartIsBeforeAndEndIsAfter(
                List.of(item1.getId()), now, now, Sort.by(Sort.Direction.DESC, "start"));


        assertThat(bookings)
                .hasSize(1)
                .containsExactly(booking2);
    }

    @Test
    void findByItemIdInAndEndDateIsBeforePastBookingsForItemIds() {

        List<Booking> bookings = bookingRepository.findByItemIdInAndEndIsBefore(
                List.of(item1.getId()), now, Sort.by(Sort.Direction.DESC, "start"));


        assertThat(bookings)
                .hasSize(1)
                .containsExactly(booking1);
    }

    @Test
    void findByItemIdInAndStartDateIsAfterFutureBookingsForItemIds() {

        List<Booking> bookings = bookingRepository.findByItemIdInAndStartIsAfter(
                List.of(item2.getId()), now, Sort.by(Sort.Direction.DESC, "start"));


        assertThat(bookings)
                .hasSize(1)
                .containsExactly(booking3);
    }

    @Test
    void getLastBookingRecentPastBooking() {

        Booking lastBooking = bookingRepository.getLastBooking(item1.getId(), now);


        assertThat(lastBooking)
                .isNotNull()
                .isEqualTo(booking1);
    }

    @Test
    void getNextBookingApprovedBooking() {
        // Arrange
        Booking futureBooking = new Booking();
        futureBooking.setBooker(user2);
        futureBooking.setItem(item1);
        futureBooking.setStart(now.plusDays(1));
        futureBooking.setEnd(now.plusDays(2));
        futureBooking.setStatus(BookingStatus.APPROVED);
        entityManager.persist(futureBooking);
        entityManager.flush();


        Booking nextBooking = bookingRepository.getNextBooking(item1.getId(), now);


        assertThat(nextBooking)
                .isNotNull()
                .isEqualTo(futureBooking);
    }

    @Test
    void getNextBookinRejectedBooking() {

        Booking rejectedBooking = new Booking();
        rejectedBooking.setBooker(user2);
        rejectedBooking.setItem(item1);
        rejectedBooking.setStart(now.plusDays(1));
        rejectedBooking.setEnd(now.plusDays(2));
        rejectedBooking.setStatus(BookingStatus.REJECTED);
        entityManager.persist(rejectedBooking);
        entityManager.flush();


        Booking nextBooking = bookingRepository.getNextBooking(item1.getId(), now);


        assertThat(nextBooking).isNull();
    }

    @Test
    void findCompletedBookingsByUser() {
        List<Booking> bookings = bookingRepository.findCompletedBookingsByUser(
                user1.getId(), item1.getId(), now);


        assertThat(bookings)
                .hasSize(1)
                .containsExactly(booking1);
    }
}