package ru.practicum.shareit.request;


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
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(RequestController.class)
public class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ItemService itemService;


    @MockBean
    private RequestService requestService;

    @MockBean
    private RequestMapper requestMapper;

    @InjectMocks
    private RequestController requestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRequest() throws Exception {
        Long userId = 1L;
        Long requestId = 1L;
        Request mockRequest = new Request();
        RequestDto mockRequestDto = new RequestDto();

        when(requestService.getRequest(userId, requestId)).thenReturn(mockRequest);
        when(requestMapper.toRequestDto(mockRequest)).thenReturn(mockRequestDto);

        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value(mockRequestDto.getDescription()));
    }


    @Test
    public void testGetAllRequests() throws Exception {
        Long userId = 1L;
        List<Request> mockRequests = Arrays.asList(new Request(), new Request());
        List<RequestDto> mockRequestDtos = Arrays.asList(new RequestDto(), new RequestDto());

        when(requestService.getUserRequests(userId)).thenReturn(mockRequests);
        when(requestMapper.toRequestDto(any(Request.class))).thenReturn(mockRequestDtos.get(0));
        when(requestMapper.toRequestDto(any(Request.class))).thenReturn(mockRequestDtos.get(1));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testCreateRequest() throws Exception {
        Long userId = 1L;
        RequestDto requestDto = new RequestDto();
        requestDto.setUserId(userId);
        Request mockRequest = new Request();
        Request mockCreatedRequest = new Request();
        UserDto newUser = new UserDto(userId, "testuser", "testemail.com");

        when(userService.getUserById(userId)).thenReturn(newUser);
        when(requestMapper.fromRequestDtoToRequest(requestDto)).thenReturn(mockRequest);
        when(requestService.createRequest(mockRequest)).thenReturn(mockCreatedRequest);
        when(requestMapper.toRequestDto(mockCreatedRequest)).thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"someField\":\"someValue\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(requestDto.getUserId()));
    }


    @Test
    public void testGetOthersRequests() throws Exception {
        Long userId = 1L;
        List<Request> mockRequests = Arrays.asList(new Request(), new Request());
        List<RequestDto> mockRequestDtos = Arrays.asList(new RequestDto(), new RequestDto());

        when(requestService.getExcludingUserRequests(userId)).thenReturn(mockRequests);
        when(requestMapper.toRequestDto(any(Request.class))).thenReturn(mockRequestDtos.get(0)).thenReturn(mockRequestDtos.get(1));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }


}

