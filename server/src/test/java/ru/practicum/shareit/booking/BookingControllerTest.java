package ru.practicum.shareit.booking;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResultDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserService userService;

    @InjectMocks
    private BookingController bookingController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Booking booking;
    private BookingRequestDto bookingRequestDto;
    private BookingResultDto bookingResultDto;

    private UserDto userDto;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setOwnerId(2L);
        itemDto.setName("name");
        itemDto.setDescription("description");

        userDto = new UserDto(1L, "user", "user@mail.com");
        bookingRequestDto = new BookingRequestDto(1L, LocalDateTime.now(),
                LocalDateTime.now().plusWeeks(2));
        bookingResultDto = new BookingResultDto(1L, LocalDateTime.now(),
                LocalDateTime.now().plusWeeks(2), BookingStatus.APPROVED, itemDto, userDto);

    }

    @Test
    void getBookingByIdTest() throws Exception {
        Long bookingId = 1L;
        Long userId = 1L;

        when(bookingService.getBookingByIdAndUserId(bookingId, userId)).thenReturn(bookingResultDto);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.item").value(itemDto));

        verify(bookingService).getBookingByIdAndUserId(bookingId, userId);
    }

    @Test
    public void getAllBookingsTest() throws Exception {
        Long userId = 1L;
        String state = "ALL";
        List<BookingResultDto> resultDtos = new ArrayList<>();
        resultDtos.add(bookingResultDto);

        when(bookingService.getBookingsByUserIdAndState(userId, state)).thenReturn(resultDtos);

        mockMvc.perform(get("/bookings?state=ALL")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

        verify(bookingService, times(1)).getBookingsByUserIdAndState(userId, state);
    }

    @Test
    public void getAllBookingsInvalidStateEmptyTest() throws Exception {
        Long userId = 1L;
        String state = "ALL";
        List<BookingResultDto> resultDtos = new ArrayList<>();
        resultDtos.add(bookingResultDto);

        when(bookingService.getBookingsByUserIdAndState(userId, state)).thenReturn(resultDtos);

        mockMvc.perform(get("/bookings?state=INVALID")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    public void getAllBookingsOwnerTest() throws Exception {
        Long userId = 1L;
        String state = "ALL";
        List<BookingResultDto> resultDtos = new ArrayList<>();
        resultDtos.add(bookingResultDto);

        when(bookingService.getBookingsByOwnerIdAndState(userId, state)).thenReturn(resultDtos);

        mockMvc.perform(get("/bookings/owner?state=ALL")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

        verify(bookingService, times(1)).getBookingsByOwnerIdAndState(userId, state);
    }

    @Test
    public void addBookingTest() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userDto);
        when(itemService.getByItemIdAndUserId(bookingRequestDto.getItemId(), 1L)).thenReturn(itemDto);

        when(bookingService.addBooking(any(ItemDto.class), any(UserDto.class), any(BookingRequestDto.class)))
                .thenReturn(bookingResultDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.id").value(1L))
                .andExpect(jsonPath("$.booker.id").value(1L))
                .andExpect(jsonPath("$.status").value("APPROVED"));

        verify(bookingService, times(1)).addBooking(any(ItemDto.class), any(UserDto.class), any(BookingRequestDto.class));
    }

    @Test
    void approveBookingTest() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = true;

        when(bookingService.approveBooking(userId, approved, bookingId)).thenReturn(bookingResultDto);

        mockMvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingService).approveBooking(userId, approved, bookingId);
    }

}