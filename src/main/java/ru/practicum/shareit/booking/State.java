package ru.practicum.shareit.booking;

import java.util.Optional;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static boolean isValid(String state) {
        try {
            State.valueOf(state);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static Optional<State> from(String stateString) {
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(stateString)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();


    }
}
