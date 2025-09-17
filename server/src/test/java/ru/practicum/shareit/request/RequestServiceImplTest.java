package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
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
public class RequestServiceImplTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private ItemMapper itemMapper;

    private UserDto owner;
    private UserDto user;
    private ItemDto item;
    private Request request;

    @BeforeEach
    void setUp() {
        UserDto ownerDto = new UserDto();
        ownerDto.setName("Katja");
        ownerDto.setEmail("katja@mail.com");
        owner = userService.createUser(ownerDto);

        item = new ItemDto();
        item.setName("sheep");
        item.setDescription("small sheep");
        item.setAvailable(true);
        item.setOwnerId(owner.getId());
        item = itemService.addItem(item, owner);

        UserDto userDto = new UserDto();
        userDto.setName("Katja2");
        userDto.setEmail("katja2@mail.com");
        user = userService.createUser(userDto);

        request = new Request();
        request.setDescription("new description");
        request.setCreated(LocalDateTime.now().minusHours(20));
        request.setUserId(user.getId());
        Item itemToAdd = itemMapper.fromDtoToItem(item, owner);
        request.setItems(List.of(itemToAdd));
        requestService.createRequest(request);
    }

    @Test
    void getUserRequestsTest() {
        List<Request> requests = requestService.getUserRequests(user.getId());

        assertThat(requests).hasSize(1);
        assertThat(requests.get(0).getUserId()).isEqualTo(user.getId());
        assertThat(requests.get(0).getDescription()).isEqualTo(request.getDescription());
    }

    @Test
    void getExcludingUserRequestsTest() {
        UserDto otherUserDto = new UserDto();
        otherUserDto.setName("Other");
        otherUserDto.setEmail("other@mail.com");
        UserDto otherUser = userService.createUser(otherUserDto);

        Request otherRequest = new Request();
        otherRequest.setDescription("other description");
        otherRequest.setCreated(LocalDateTime.now().minusHours(10));
        otherRequest.setUserId(otherUser.getId());
        otherRequest.setItems(List.of());
        requestService.createRequest(otherRequest);


        List<Request> requests = requestService.getExcludingUserRequests(user.getId());

        assertThat(requests).hasSize(1);
        assertThat(requests.get(0).getUserId()).isEqualTo(otherUser.getId());
        assertThat(requests.get(0).getDescription()).isEqualTo(otherRequest.getDescription());
    }

    @Test
    void getExcludingUserRequestsEmptyListTest() {
        List<Request> requests = requestService.getExcludingUserRequests(owner.getId());

        assertThat(requests).hasSize(1);
        assertThat(requests.get(0).getUserId()).isEqualTo(user.getId());
        assertThat(requests.get(0).getDescription()).isEqualTo(request.getDescription());
    }
}