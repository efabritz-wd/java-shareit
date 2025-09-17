package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResultDto;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "jdbc.url=jdbc:postgresql://localhost:5432/test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = ru.practicum.shareit.ShareItServer.class)
public class BookingServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    private UserDto booker;
    private UserDto owner;
    private ItemDto item1;
    private ItemDto item2;
    private BookingRequestDto bookingRequest1;
    private BookingRequestDto bookingRequest2;

    @BeforeEach
    void setUp() {
        // Create booker
        UserDto bookerDto = new UserDto();
        bookerDto.setName("testUser");
        bookerDto.setEmail("test@mail.com");
        booker = userService.createUser(bookerDto);

        // Create owner
        UserDto ownerDto = new UserDto();
        ownerDto.setName("testUser2");
        ownerDto.setEmail("test2@mail.com");
        owner = userService.createUser(ownerDto);

        // Create items
        item1 = new ItemDto();
        item1.setOwnerId(owner.getId());
        item1.setAvailable(true);
        item1.setName("name1");
        item1.setDescription("description1");
        item1 = itemService.addItem(item1, owner);

        item2 = new ItemDto();
        item2.setOwnerId(owner.getId());
        item2.setAvailable(true);
        item2.setName("name2");
        item2.setDescription("description2");
        item2 = itemService.addItem(item2, owner);

        bookingRequest1 = new BookingRequestDto(item1.getId(),
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(5));
        bookingRequest2 = new BookingRequestDto(item2.getId(),
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(7));
    }

    @Test
    void createBookingTest() {
        BookingResultDto createdBooking = bookingService.addBooking(item1, booker, bookingRequest1);
        bookingService.approveBooking(owner.getId(), true, createdBooking.getId());

        List<Booking> bookings = bookingRepository.findByBookerId(booker.getId());
        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getId()).isEqualTo(createdBooking.getId());
        assertThat(bookings.get(0).getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void getAllBookingsTest() {
        bookingService.addBooking(item1, booker, bookingRequest1);
        bookingService.addBooking(item2, booker, bookingRequest2);


        List<Booking> bookings = bookingRepository.findByBookerId(booker.getId());
        assertThat(bookings).hasSize(2);
    }

    @Test
    void getBookingsByOwnerIdAndStateAllTest() {
        BookingResultDto booking1 = bookingService.addBooking(item1, booker, bookingRequest1);
        BookingResultDto booking2 = bookingService.addBooking(item2, booker, bookingRequest2);

        List<BookingResultDto> bookings = bookingService.getBookingsByOwnerIdAndState(owner.getId(), "ALL");

        assertThat(bookings).hasSize(2);
        assertThat(bookings).extracting(BookingResultDto::getId).containsExactlyInAnyOrder(booking1.getId(), booking2.getId());
    }

    @Test
    void getBookingsByOwnerIdAndStateCurrentTest() {
        BookingRequestDto currentBooking = new BookingRequestDto(item1.getId(),
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
        BookingResultDto booking = bookingService.addBooking(item1, booker, currentBooking);

        List<BookingResultDto> bookings = bookingService.getBookingsByOwnerIdAndState(owner.getId(), "CURRENT");

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getId()).isEqualTo(booking.getId());
    }

    @Test
    void getBookingsByOwnerIdAndStatePastTest() {

        BookingRequestDto pastBooking = new BookingRequestDto(item1.getId(),
                LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(2));
        BookingResultDto booking = bookingService.addBooking(item1, booker, pastBooking);

        List<BookingResultDto> bookings = bookingService.getBookingsByOwnerIdAndState(owner.getId(), "PAST");

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getId()).isEqualTo(booking.getId());
    }

    @Test
    void getBookingsByOwnerIdAndStateFutureTest() {
        BookingResultDto booking = bookingService.addBooking(item1, booker, bookingRequest1);
        List<BookingResultDto> bookings = bookingService.getBookingsByOwnerIdAndState(owner.getId(), "FUTURE");

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getId()).isEqualTo(booking.getId());
    }

    @Test
    void getBookingsByOwnerIdAndStateWaitingTest() {
        BookingResultDto booking = bookingService.addBooking(item1, booker, bookingRequest1);
        List<BookingResultDto> bookings = bookingService.getBookingsByOwnerIdAndState(owner.getId(), "WAITING");


        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getId()).isEqualTo(booking.getId());
        assertThat(bookings.get(0).getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void getBookingsByOwnerIdAndStateRejectedTest() {
        BookingResultDto booking = bookingService.addBooking(item1, booker, bookingRequest1);
        bookingService.approveBooking(owner.getId(), false, booking.getId());

        List<BookingResultDto> bookings = bookingService.getBookingsByOwnerIdAndState(owner.getId(), "REJECTED");


        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getId()).isEqualTo(booking.getId());
        assertThat(bookings.get(0).getStatus()).isEqualTo(BookingStatus.REJECTED);
    }

    @Test
    void getBookingsByOwnerIdAndStateInvalidStateThrowsExceptionTest() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.getBookingsByOwnerIdAndState(owner.getId(), "INVALID"));
        assertThat(exception.getMessage()).isEqualTo("State parameter is not valid");
    }

    @Test
    void getBookingsByUserIdAndStateAllTest() {
        BookingResultDto booking1 = bookingService.addBooking(item1, booker, bookingRequest1);
        BookingResultDto booking2 = bookingService.addBooking(item2, booker, bookingRequest2);

        List<BookingResultDto> bookings = bookingService.getBookingsByUserIdAndState(booker.getId(), "ALL");

        assertThat(bookings).hasSize(2);
        assertThat(bookings).extracting(BookingResultDto::getId).containsExactlyInAnyOrder(booking1.getId(), booking2.getId());
    }

    @Test
    void getBookingsByUserIdAndStateCurrentTest() {
        BookingRequestDto currentBooking = new BookingRequestDto(item1.getId(),
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
        BookingResultDto booking = bookingService.addBooking(item1, booker, currentBooking);

        List<BookingResultDto> bookings = bookingService.getBookingsByUserIdAndState(booker.getId(), "CURRENT");

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getId()).isEqualTo(booking.getId());
    }

    @Test
    void getBookingsByUserIdAndStatePastTest() {
        BookingRequestDto pastBooking = new BookingRequestDto(item1.getId(),
                LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(2));
        BookingResultDto booking = bookingService.addBooking(item1, booker, pastBooking);

        List<BookingResultDto> bookings = bookingService.getBookingsByUserIdAndState(booker.getId(), "PAST");

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getId()).isEqualTo(booking.getId());
    }

    @Test
    void getBookingsByUserIdAndStateFutureTest() {
        BookingResultDto booking = bookingService.addBooking(item1, booker, bookingRequest1);

        List<BookingResultDto> bookings = bookingService.getBookingsByUserIdAndState(booker.getId(), "FUTURE");

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getId()).isEqualTo(booking.getId());
    }

    @Test
    void getBookingsByUserIdAndStateWaitingTest() {
        BookingResultDto booking = bookingService.addBooking(item1, booker, bookingRequest1);

        List<BookingResultDto> bookings = bookingService.getBookingsByUserIdAndState(booker.getId(), "WAITING");

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getId()).isEqualTo(booking.getId());
        assertThat(bookings.get(0).getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void getBookingsByUserIdAndStateRejectedTest() {
        BookingResultDto booking = bookingService.addBooking(item1, booker, bookingRequest1);
        bookingService.approveBooking(owner.getId(), false, booking.getId());

        List<BookingResultDto> bookings = bookingService.getBookingsByUserIdAndState(booker.getId(), "REJECTED");

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getId()).isEqualTo(booking.getId());
        assertThat(bookings.get(0).getStatus()).isEqualTo(BookingStatus.REJECTED);
    }

    @Test
    void getBookingsByUserIdAndStateInvalidStateThrowsExceptionTest() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.getBookingsByUserIdAndState(booker.getId(), "INVALID"));
        assertThat(exception.getMessage()).isEqualTo("State parameter is not valid");
    }
}