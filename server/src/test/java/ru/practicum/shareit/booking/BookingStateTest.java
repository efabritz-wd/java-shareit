package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingStateTest {

    @Test
    void isValidStateTest() {
        assertThat(State.isValid("ALL")).isTrue();
        assertThat(State.isValid("CURRENT")).isTrue();
        assertThat(State.isValid("PAST")).isTrue();
        assertThat(State.isValid("FUTURE")).isTrue();
        assertThat(State.isValid("WAITING")).isTrue();
        assertThat(State.isValid("REJECTED")).isTrue();
    }

    @Test
    void isValidInvalidStateTest() {
        assertThat(State.isValid("INVALID")).isFalse();
        assertThat(State.isValid("")).isFalse();
    }

    @Test
    void correctStateTest() {
        assertThat(State.from("ALL")).isEqualTo(Optional.of(State.ALL));
        assertThat(State.from("all")).isEqualTo(Optional.of(State.ALL));
        assertThat(State.from("CURRENT")).isEqualTo(Optional.of(State.CURRENT));
        assertThat(State.from("current")).isEqualTo(Optional.of(State.CURRENT));
        assertThat(State.from("PAST")).isEqualTo(Optional.of(State.PAST));
        assertThat(State.from("past")).isEqualTo(Optional.of(State.PAST));
        assertThat(State.from("FUTURE")).isEqualTo(Optional.of(State.FUTURE));
        assertThat(State.from("future")).isEqualTo(Optional.of(State.FUTURE));
        assertThat(State.from("WAITING")).isEqualTo(Optional.of(State.WAITING));
        assertThat(State.from("waiting")).isEqualTo(Optional.of(State.WAITING));
        assertThat(State.from("REJECTED")).isEqualTo(Optional.of(State.REJECTED));
        assertThat(State.from("rejected")).isEqualTo(Optional.of(State.REJECTED));
    }

    @Test
    void invalidStateTest() {
        assertThat(State.from("INVALID")).isEmpty();
        assertThat(State.from("")).isEmpty();
        assertThat(State.from(null)).isEmpty();
    }

    @Test
    void enumValuesTest() {
        State[] expectedValues = {State.ALL, State.CURRENT, State.PAST, State.FUTURE, State.WAITING, State.REJECTED};
        assertThat(State.values()).containsExactly(expectedValues);
    }
}