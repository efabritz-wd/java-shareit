package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final ItemService itemService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final RequestService requestService;

    @Autowired
    private final ItemMapper itemMapper;

    @Test
    public void getUserRequests() {
        UserDto createdOwnerDto = new UserDto();
        createdOwnerDto.setName("Katja");
        createdOwnerDto.setEmail("katja@mail.com");
        UserDto createdOwner = userService.createUser(createdOwnerDto);

        ItemDto itemFirst = new ItemDto();

        itemFirst.setName("sheep");
        itemFirst.setDescription("small sheep");
        itemFirst.setAvailable(true);
        itemFirst.setOwnerId(createdOwner.getId());

        ItemDto createdItem = itemService.addItem(itemFirst, createdOwner);
        Item itemToAdd = itemMapper.fromDtoToItem(createdItem, createdOwner);

        UserDto userDto = new UserDto();
        userDto.setName("Katja2");
        userDto.setEmail("katja2@mail.com");
        UserDto createdUser = userService.createUser(userDto);

        Request request = new Request();
        request.setDescription("new description");
        request.setCreated(LocalDateTime.now().minusHours(20));
        request.setUserId(createdUser.getId());
        request.setItems(List.of(itemToAdd));

        Request createdRequest = requestService.createRequest(request);

        List<Request> requests = requestService.getUserRequests(createdUser.getId());

        assertThat(requests.size()).isEqualTo(1);
        if (!requests.isEmpty()) {
            assertThat(requests.get(0).userId).isEqualTo(createdUser.getId());
            assertThat(requests.get(0).getDescription()).isEqualTo(createdRequest.getDescription());
        }
    }
}
