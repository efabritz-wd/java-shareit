package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BookingTest {
    @Test
    void testBookingEquality() {
        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStatus(BookingStatus.WAITING);

        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStatus(BookingStatus.WAITING);

        Booking booking3 = new Booking();
        booking3.setId(2L);
        booking3.setStatus(BookingStatus.APPROVED);

        Assertions.assertFalse(booking1.equals(booking2));
        Assertions.assertTrue(booking2.equals(booking3));
    }

    @Test
    void testHashCode() {
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStatus(BookingStatus.WAITING);

        Booking booking3 = new Booking();
        booking3.setId(2L);
        booking3.setStatus(BookingStatus.APPROVED);

        Assertions.assertEquals(booking3.hashCode(), booking2.hashCode());
    }
}
