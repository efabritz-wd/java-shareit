package ru.practicum.shareit.item;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(
        properties = "jdbc.url=jdbc:postgresql://localhost:5432/test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = ru.practicum.shareit.ShareItServer.class)
public class ItemServiceImplTest {

    @Autowired
    private final ItemService itemService;

    @Autowired
    private final UserService userService;

    @Test
    public void getItemsByUserIdTest() {
        UserDto createdOwnerDto = new UserDto();
        createdOwnerDto.setName("Katja");
        createdOwnerDto.setEmail("katja@mail.com");

        UserDto createdOwner = userService.createUser(createdOwnerDto);

        UserDto createdOwner2Dto = new UserDto();
        createdOwner2Dto.setName("Kolja");
        createdOwner2Dto.setEmail("kolja@mail.com");

        UserDto createdOwner2 = userService.createUser(createdOwner2Dto);

        ItemDto itemFirst = new ItemDto();
        ItemDto itemSecond = new ItemDto();
        ItemDto itemThird = new ItemDto();

        itemFirst.setName("sheep");
        itemFirst.setDescription("small sheep");
        itemFirst.setAvailable(true);
        itemFirst.setOwnerId(createdOwner.getId());

        itemSecond.setName("ring");
        itemSecond.setDescription("small ring");
        itemSecond.setAvailable(false);
        itemSecond.setOwnerId(createdOwner.getId());

        itemThird.setName("vitamin");
        itemThird.setDescription("small vitamin");
        itemThird.setAvailable(true);
        itemThird.setOwnerId(createdOwner2.getId());

        itemService.addItem(itemFirst, createdOwner);
        ItemDto item2 = itemService.addItem(itemSecond, createdOwner);
        ItemDto item3 = itemService.addItem(itemThird, createdOwner2);

        List<ItemDto> items1List = itemService.getItemsByUserId(createdOwner.getId());
        List<ItemDto> items2List = itemService.getItemsByUserId(createdOwner2.getId());

        assertThat(items1List.size()).isEqualTo(2);
        assertThat(items2List.size()).isEqualTo(1);

        assertThat(items1List).contains(item2);
        assertThat(items2List).contains(item3);
    }
}

