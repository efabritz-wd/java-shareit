package ru.practicum.shareit.item;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserService userService;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetItemById() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        ItemDto mockItemDto = new ItemDto(1L, "itemname", "description",
                false, 1L, null, null, null, null);

        when(userService.getUserById(userId)).thenReturn(new UserDto(userId, "testuser", "testemail.com"));
        when(itemService.getByItemIdAndUserId(itemId, userId)).thenReturn(mockItemDto);

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(mockItemDto.getName()));
    }


    @Test
    public void testGetItemsByUserId() throws Exception {
        Long userId = 1L;
        List<ItemDto> mockItems = Arrays.asList(new ItemDto(), new ItemDto());

        when(itemService.getItemsByUserId(userId)).thenReturn(mockItems);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }


    @Test
    public void testGetItemsByText() throws Exception {
        Long userId = 1L;
        List<ItemDto> mockItems = Arrays.asList(new ItemDto(), new ItemDto());

        when(userService.getUserById(userId)).thenReturn(new UserDto(userId, "testuser", "testemail.com"));
        when(itemService.getAllItemsByText("test")).thenReturn(mockItems);

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", "test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }


    @Test
    public void testAddItem() throws Exception {
        Long userId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setOwnerId(userId);
        ItemDto mockCreatedItem = new ItemDto();
        mockCreatedItem.setId(1L);
        mockCreatedItem.setName("Test Item");
        mockCreatedItem.setDescription("Test Description");
        mockCreatedItem.setAvailable(true);

        when(userService.getUserById(userId)).thenReturn(new UserDto(userId, "testuser", "testemail.com"));
        when(itemService.addItem(any(ItemDto.class), any(UserDto.class))).thenReturn(mockCreatedItem);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Item\", \"description\":\"Test Description\", \"available\":\"true\"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Item"));
    }


    @Test
    public void testUpdateItem() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        ItemDto mockUpdatedItem = new ItemDto();

        when(userService.getUserById(userId)).thenReturn(new UserDto(userId, "testuser", "testemail.com"));
        when(itemService.updateItem(any(ItemDto.class), eq(itemId), any(UserDto.class))).thenReturn(mockUpdatedItem);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Item\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    public void testDeleteItem() throws Exception {
        Long itemId = 1L;

        doNothing().when(userService).deleteUser(itemId);

        mockMvc.perform(delete("/items/{itemId}", itemId))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddComment() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        CommentDto mockCommentDto = new CommentDto(1L, "New item comment", "testuser", LocalDateTime.now().minusHours(4));

        when(itemService.getByItemIdAndUserId(itemId, userId)).thenReturn(new ItemDto());
        when(userService.getUserById(userId)).thenReturn(new UserDto(userId, "testuser", "testemail.com"));
        when(itemService.addComment(any(CommentDto.class), any(UserDto.class), any(ItemDto.class))).thenReturn(mockCommentDto);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"New item comment\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("New item comment"));
    }

}

