package ru.practicum.shareit.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerTest {

    private ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler();
    }

    @Test
    void validationExceptionCommon_shouldReturnBadRequest() {
        ValidationException ex = new ValidationException("Validation failed");

        ErrorResponse response = errorHandler.validationExceptionCommon(ex);

        assertEquals("Error during validation", response.getError());
        assertEquals("Validation failed", response.getDescription());
    }

    @Test
    void notFoundException_shouldReturnNotFound() {
        NotFoundException ex = new NotFoundException("User not found");

        ErrorResponse response = errorHandler.notFoundException(ex);

        assertEquals("User not found", response.getError());
        assertEquals("User not found", response.getDescription());
    }

    @Test
    void forbiddenException_shouldReturnForbidden() {
        ForbiddenException ex = new ForbiddenException("Access denied");

        ErrorResponse response = errorHandler.forbiddenException(ex);

        assertEquals("Access forbidden", response.getError());
        assertEquals("Access denied", response.getDescription());
    }

    @Test
    void commonException_shouldReturnInternalServerError() {
        ConditionsNotMetException ex = new ConditionsNotMetException("Conditions not met");

        ErrorResponse response = errorHandler.commonException(ex);

        assertEquals("Error", response.getError());
        assertEquals("Conditions not met", response.getDescription());
    }

    @Test
    void handleAllExceptions_shouldReturnInternalServerError() {
        Exception ex = new Exception("Unexpected error");

        ErrorResponse response = errorHandler.handleAllExceptions(ex);

        assertEquals("Error", response.getError());
        assertEquals("Error unknown", response.getDescription());
    }
}