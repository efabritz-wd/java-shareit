package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.BookingService;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResultDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "jdbc.url=jdbc:postgresql://localhost:5432/test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = ShareItServer.class)
public class ItemServiceImplTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    private UserDto owner1;
    private UserDto owner2;
    private ItemDto item1;
    private ItemDto item2;
    private ItemDto item3;
    private CommentDto comment1;
    private CommentDto comment2;

    ItemDto createdItem1;
    ItemDto createdItem2;
    ItemDto createdItem3;

    @BeforeEach
    void setUp() {
        UserDto owner1Dto = new UserDto();
        owner1Dto.setName("Katja");
        owner1Dto.setEmail("katja@mail.com");
        owner1 = userService.createUser(owner1Dto);

        UserDto owner2Dto = new UserDto();
        owner2Dto.setName("Kolja");
        owner2Dto.setEmail("kolja@mail.com");
        owner2 = userService.createUser(owner2Dto);

        item1 = new ItemDto();
        item1.setName("sheep");
        item1.setDescription("small sheep");
        item1.setAvailable(true);
        item1.setOwnerId(owner1.getId());

        item2 = new ItemDto();
        item2.setName("ring");
        item2.setDescription("small ring");
        item2.setAvailable(false);
        item2.setOwnerId(owner1.getId());

        item3 = new ItemDto();
        item3.setName("vitamin");
        item3.setDescription("small vitamin");
        item3.setAvailable(true);
        item3.setOwnerId(owner2.getId());

        createdItem1 = itemService.addItem(item1, owner1);
        createdItem2 = itemService.addItem(item2, owner1);
        createdItem3 = itemService.addItem(item3, owner2);
    }

    @Test
    void getItemsByUserIdTest() {
        List<ItemDto> items1List = itemService.getItemsByUserId(owner1.getId());
        List<ItemDto> items2List = itemService.getItemsByUserId(owner2.getId());

        assertThat(items1List).hasSize(2).contains(createdItem1, createdItem2);
        assertThat(items2List).hasSize(1).contains(createdItem3);
    }

    @Test
    void getItemsByText() {
        List<ItemDto> items1List = itemService.getAllItemsByText("rong");
        List<ItemDto> items2List = itemService.getAllItemsByText("Sheep");
        List<ItemDto> items3List = itemService.getAllItemsByText("vitamin");

        assertThat(items1List).isEmpty();
        assertThat(items2List).hasSize(1).contains(createdItem1);
        assertThat(items3List).hasSize(1).contains(createdItem3);
    }

    @Test
    void addCommentSuccessTest() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto(createdItem1.getId(),
                LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1));
        BookingResultDto bookingResultDto = bookingService.addBooking(createdItem1, owner2, bookingRequestDto);
        bookingService.approveBooking(owner1.getId(), true, bookingResultDto.getId());
        bookingService.getBookingsByUserIdAndState(owner2.getId(), "ALL");

        comment2 = new CommentDto(2L, "bad item", "author new name", LocalDateTime.now());
        CommentDto addedComment = itemService.addComment(comment2, owner2, createdItem1);

        assertThat(addedComment.getText()).isEqualTo("bad item");
        assertThat(addedComment.getAuthorName()).isEqualTo("Kolja");
    }

    @Test
    void addCommentExceptionTest() {
        comment1 = new CommentDto(1L, "good item", "auther name", LocalDateTime.now());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.addComment(comment1, owner1, item3);
        });

        assertEquals("User did not rent this item.", exception.getMessage());
    }

    @Test
    void getAllCommentsByItemIdTest() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto(createdItem1.getId(),
                LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1));
        BookingResultDto bookingResultDto = bookingService.addBooking(createdItem1, owner2, bookingRequestDto);
        bookingService.approveBooking(owner1.getId(), true, bookingResultDto.getId());
        bookingService.getBookingsByUserIdAndState(owner2.getId(), "ALL");

        comment2 = new CommentDto(3L, "Great item", "Very useful!", LocalDateTime.now());

        itemService.addComment(comment2, owner2, createdItem1);
        List<CommentDto> comments = itemService.getAllCommentsByItemId(createdItem1.getId());

        assertThat(comments).hasSize(1);
        assertThat(comments).extracting(CommentDto::getText).containsExactlyInAnyOrder("Great item");
    }

    @Test
    void getAllCommentsByItemIdEmptyListTest() {
        List<CommentDto> comments = itemService.getAllCommentsByItemId(item2.getId());

        assertThat(comments).isEmpty();
    }
}