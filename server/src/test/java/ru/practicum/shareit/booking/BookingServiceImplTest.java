package ru.practicum.shareit.booking;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResultDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(
        properties = "jdbc.url=jdbc:postgresql://localhost:5432/test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = ru.practicum.shareit.ShareItServer.class)
public class BookingServiceImplTest {
    @Autowired
    private final UserService userService;

    @Autowired
    private final ItemService itemService;

    @Autowired
    private final BookingService bookingService;

    @Autowired
    private final BookingRepository bookingRepository;


    @Test
    void createBookingTest() {
        UserDto testUser = new UserDto();
        testUser.setName("testUser");
        testUser.setEmail("test@mail.com");

        UserDto createdUser = userService.createUser(testUser);

        UserDto testUser2 = new UserDto();
        testUser2.setName("testUser2");
        testUser2.setEmail("tes2t@mail.com");

        UserDto createdUser2 = userService.createUser(testUser2);

        ItemDto itemDto = new ItemDto();
        itemDto.setOwnerId(createdUser2.getId());
        itemDto.setAvailable(true);
        itemDto.setName("name");
        itemDto.setDescription("description");

        ItemDto createdItem = itemService.addItem(itemDto, createdUser2);

        BookingRequestDto bookingRequestDto = new BookingRequestDto(createdItem.getId(),
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(5));

        BookingResultDto createdBookingRequest = bookingService.addBooking(createdItem, createdUser, bookingRequestDto);

        assertThat(createdBookingRequest.getId()).isNotNull();
        List<Booking> bookings = bookingRepository.findByBookerId(createdUser.getId());
        assertThat(bookings.size()).isEqualTo(1);
        if (bookings.size() == 1) {
            assertThat(bookings.get(0).getStatus().equals(BookingStatus.WAITING));
            assertThat(bookings.get(0).getId().equals(createdBookingRequest.getId()));
        }

        bookingService.approveBooking(createdUser2.getId(),
                true, bookings.get(0).getId());

        List<Booking> bookingApprovedDtos = bookingRepository.findByBookerId(createdUser.getId());

        if (bookingApprovedDtos.size() == 1) {
            assertThat(bookingApprovedDtos.get(0).getStatus()).isEqualByComparingTo(BookingStatus.APPROVED);
        }

    }

    @Test
    void getAllBookingsTest() {
        UserDto testUser = new UserDto();
        testUser.setName("testUser");
        testUser.setEmail("test@mail.com");

        UserDto createdUser = userService.createUser(testUser);

        UserDto testUser2 = new UserDto();
        testUser2.setName("testUser2");
        testUser2.setEmail("tes2t@mail.com");

        UserDto createdUser2 = userService.createUser(testUser2);

        ItemDto itemDto = new ItemDto();
        itemDto.setOwnerId(createdUser2.getId());
        itemDto.setAvailable(true);
        itemDto.setName("name");
        itemDto.setDescription("description");

        ItemDto createdItem = itemService.addItem(itemDto, createdUser2);

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setOwnerId(createdUser2.getId());
        itemDto2.setAvailable(true);
        itemDto2.setName("name");
        itemDto2.setDescription("description");

        ItemDto createdItem2 = itemService.addItem(itemDto2, createdUser2);

        BookingRequestDto bookingRequestDto = new BookingRequestDto(createdItem.getId(),
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(5));

        BookingRequestDto bookingRequestDto2 = new BookingRequestDto(createdItem2.getId(),
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(7));

        bookingService.addBooking(createdItem, createdUser, bookingRequestDto);
        bookingService.addBooking(createdItem2, createdUser, bookingRequestDto2);

        assertThat(bookingRepository.findByBookerId(createdUser.getId()).size()).isEqualTo(2);
    }

}
