package ru.practicum.shareit.item;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    private User user1;
    private User user2;
    private Item item1;
    private Item item2;
    private Item item3;

    @BeforeEach
    void setUp() {

        user1 = new User();
        user1.setName("User1");
        user1.setEmail("user1@mail.com");
        entityManager.persist(user1);

        user2 = new User();
        user2.setName("User2");
        user2.setEmail("user2@mail.com");
        entityManager.persist(user2);


        item1 = new Item();
        item1.setName("item1");
        item1.setDescription("item1 desc");
        item1.setAvailable(true);
        item1.setOwner(user1);
        entityManager.persist(item1);

        item2 = new Item();
        item2.setName("item2");
        item2.setDescription("item2 desc");
        item2.setAvailable(true);
        item2.setOwner(user1);
        entityManager.persist(item2);

        item3 = new Item();
        item3.setName("item3");
        item3.setDescription("item3 desc");
        item3.setAvailable(false);
        item3.setOwner(user2);
        entityManager.persist(item3);

        entityManager.flush();
    }

    @Test
    void findAllByOwnerId() {

        List<Item> items = itemRepository.findAllByOwnerId(user1.getId());


        assertThat(items)
                .hasSize(2)
                .containsExactlyInAnyOrder(item1, item2);
    }

    @Test
    void findAllByOwnerIdNonExistentOwner() {

        List<Item> items = itemRepository.findAllByOwnerId(999L);


        assertThat(items).isEmpty();
    }

    @Test
    void findByIdAndOwnerId() {
        Item foundItem = itemRepository.findByIdAndOwnerId(item1.getId(), user1.getId());

        assertThat(foundItem)
                .isNotNull()
                .isEqualTo(item1);
    }

    @Test
    void findByIdAndOwnerIdItemNotFound() {
        Item foundItem = itemRepository.findByIdAndOwnerId(999L, user1.getId());

        assertThat(foundItem).isNull();
    }

    @Test
    void findByIdAndOwnerIdOwnerDoesNotMatch() {
        Item foundItem = itemRepository.findByIdAndOwnerId(item1.getId(), user2.getId());

        assertThat(foundItem).isNull();
    }

    @Test
    void searchByText() {
        List<Item> items = itemRepository.searchByText("item1");

        assertThat(items)
                .hasSize(1)
                .containsExactly(item1);
    }

    @Test
    void searchByTextCaseInsensitive() {
        List<Item> items = itemRepository.searchByText("ITEM2");

        assertThat(items)
                .hasSize(1)
                .containsExactly(item2);
    }

    @Test
    void searchByTextNonMatchingText() {
        List<Item> items = itemRepository.searchByText("wrench");

        assertThat(items).isEmpty();
    }

    @Test
    void searchByTextNotAvailableItem() {
        List<Item> items = itemRepository.searchByText("item3");

        assertThat(items).isEmpty();
    }

    @Test
    void findItemIdsByOwnerId() {
        List<Long> itemIds = itemRepository.findItemIdsByOwnerId(user1.getId());

        assertThat(itemIds)
                .hasSize(2)
                .containsExactlyInAnyOrder(item1.getId(), item2.getId());
    }

    @Test
    void findItemIdsByOwnerIdNonExistentOwner() {
        List<Long> itemIds = itemRepository.findItemIdsByOwnerId(999L);

        assertThat(itemIds).isEmpty();
    }
}