package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;

class ItemTest {

    @Test
    void testItemEquality() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("name");

        Item item2 = new Item();
        item2.setId(1L);
        item2.setName("name");

        Item item3 = new Item();
        item3.setId(2L);
        item3.setName("name");

        Assertions.assertTrue(item1.equals(item1));
        Assertions.assertTrue(item2.equals(item1));
        Assertions.assertFalse(item3.equals(item2));
    }

    @Test
    void testHashCode() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("name");

        Item item2 = new Item();
        item2.setId(1L);
        item2.setName("name");

        Assertions.assertEquals(item2.hashCode(), item1.hashCode());
    }
}
