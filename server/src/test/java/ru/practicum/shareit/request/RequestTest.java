package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RequestTest {
    @Test
    void testRequestEquality() {
        Request request1 = new Request();
        request1.setId(1L);
        request1.setDescription("description1");

        Request request2 = new Request();
        request2.setId(1L);
        request2.setDescription("description2");

        Request request3 = new Request();
        request3.setId(2L);
        request3.setDescription("description3");

        Assertions.assertTrue(request1.equals(request1));
        Assertions.assertTrue(request1.equals(request2));
        Assertions.assertFalse(request1.equals(request3));
    }

    @Test
    void testHashCode() {
        Request request1 = new Request();
        request1.setId(1L);
        request1.setDescription("description1");

        Request request2 = new Request();
        request2.setId(1L);
        request2.setDescription("description2");

        Assertions.assertEquals(request1.hashCode(), request2.hashCode());
    }
}
