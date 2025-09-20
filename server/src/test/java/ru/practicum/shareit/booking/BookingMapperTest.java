package ru.practicum.shareit.booking;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResultDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.item.dto.BookerDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingMapperTest {

    private BookingMapper bookingMapper;

    @BeforeEach
    public void setUp() {
        bookingMapper = new BookingMapper();
    }

    @Test
    public void testRequestToBooking() {

        BookingRequestDto bookingRequestDto = new BookingRequestDto(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );
        ItemDto itemDto = new ItemDto(1L, "Item Name", "Item Description",
                true, 1L, null, null, List.of(), null);
        UserDto userDto = new UserDto(1L, "User Name", "user@mail.com");
        BookingStatus status = BookingStatus.WAITING;


        Booking result = bookingMapper.requestToBooking(bookingRequestDto, itemDto, userDto, status);


        assertThat(result).isNotNull();
        assertThat(result.getStart()).isEqualTo(bookingRequestDto.getStart());
        assertThat(result.getEnd()).isEqualTo(bookingRequestDto.getEnd());
        assertThat(result.getStatus()).isEqualTo(status);
        assertThat(result.getBooker().getId()).isEqualTo(userDto.getId());
        assertThat(result.getItem().getId()).isEqualTo(itemDto.getId());
    }

    @Test
    public void testFromBookingToDto() {

        UserDto userDto = new UserDto(1L, "User Name", "user@mail.com");
        ItemDto itemDto = new ItemDto(1L, "Item Name", "Item Description",
                true, 1L, null, null, List.of(), null);
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(BookingStatus.APPROVED);
        booking.setBooker(new User());
        booking.getBooker().setId(userDto.getId());
        booking.setItem(new Item());
        booking.getItem().setId(itemDto.getId());
        booking.getItem().setName(itemDto.getName());

        User user1 = new User();
        user1.setId(1L);
        booking.getItem().setOwner(user1);


        BookingResultDto result = bookingMapper.fromBookingToDto(booking);


        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStart()).isEqualTo(booking.getStart());
        assertThat(result.getEnd()).isEqualTo(booking.getEnd());
        assertThat(result.getStatus()).isEqualTo(BookingStatus.APPROVED);
        assertThat(result.getItem().getId()).isEqualTo(itemDto.getId());
        assertThat(result.getBooker().getId()).isEqualTo(userDto.getId());
    }

    @Test
    public void testFromBookingToDtoList() {

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStart(LocalDateTime.now().plusDays(1));
        booking1.setEnd(LocalDateTime.now().plusDays(2));
        booking1.setStatus(BookingStatus.APPROVED);
        User user1 = new User();
        user1.setId(1L);
        booking1.setBooker(user1);
        Item item1 = new Item();
        item1.setId(1L);

        booking1.setItem(item1);

        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStart(LocalDateTime.now().plusDays(2));
        booking2.setEnd(LocalDateTime.now().plusDays(3));
        booking2.setStatus(BookingStatus.WAITING);
        User user2 = new User();
        user2.setId(2L);
        booking2.setBooker(user2);
        Item item2 = new Item();
        item2.setId(2L);

        booking2.setItem(item2);

        item2.setOwner(user1);
        item1.setOwner(user2);

        List<Booking> bookings = List.of(booking1, booking2);


        List<BookingResultDto> result = bookingMapper.fromBookingToDto(bookings);


        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
    }

    @Test
    public void testToBookerDto() {

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        User user = new User();
        user.setId(1L);
        booking.setBooker(user);


        BookerDto result = bookingMapper.toBookerDto(booking);


        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStart()).isEqualTo(booking.getStart());
        assertThat(result.getEnd()).isEqualTo(booking.getEnd());
        assertThat(result.getBookerId()).isEqualTo(booking.getBooker().getId());
    }
}

