package ru.practicum.shareit.errork;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ErrorHandlerTest {

    @Test
    public void testItemNotFoundHandler() {
        ErrorHandler errorHandler = new ErrorHandler();
        ItemNotFoundException exception = new ItemNotFoundException("Item not found");
        ErrorResponse response = errorHandler.itemNotFound(exception);

        Assertions.assertEquals("Item not found", response.getError());
    }

    @Test
    public void testItemUnavailableErrorHandler() {
        ErrorHandler errorHandler = new ErrorHandler();
        ItemNotAvailableException exception = new ItemNotAvailableException("Item unavailable");
        ErrorResponse response = errorHandler.itemUnavailableError(exception);

        Assertions.assertEquals("Item unavailable", response.getError());
    }
}
