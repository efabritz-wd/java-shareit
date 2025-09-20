package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testConstructorAndGetters() {
        String error = "NotFound";
        String description = "Resource not found";

        ErrorResponse errorResponse = new ErrorResponse(error, description);

        assertEquals(error, errorResponse.getError(), "Error should match input");
        assertEquals(description, errorResponse.getDescription(), "Description should match input");
    }

    @Test
    void testNullError() {
        String description = "Some description";

        ErrorResponse errorResponse = new ErrorResponse(null, description);

        assertNull(errorResponse.getError(), "Error should be null");
        assertEquals(description, errorResponse.getDescription(), "Description should match input");
    }

    @Test
    void testNullDescription() {
        String error = "NotFound";

        ErrorResponse errorResponse = new ErrorResponse(error, null);

        assertEquals(error, errorResponse.getError(), "Error field should match constructor input");
        assertNull(errorResponse.getDescription(), "Description field should be null");
    }
}