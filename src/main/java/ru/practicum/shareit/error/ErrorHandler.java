package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.error.exceptions.EntityNotFoundException;
import ru.practicum.shareit.error.exceptions.SimpleException;
import ru.practicum.shareit.error.exceptions.ValidationException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIncorrectParameterException(final ValidationException e) {
        return new ErrorResponse(
                "VALIDATION_FAILED",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleObjectNotFound(final EntityNotFoundException e) {
        return new ErrorResponse(
                "NOT_FOUND",
                e.getMessage()
        );
    }

    @ExceptionHandler(SimpleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSimpleException(SimpleException ex) {
        return new ErrorResponse(
                "CONSTRAINT_VIOLATION",
                ex.getMessage()
        );
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingHeaderError(MissingRequestHeaderException ex) {
        return new ErrorResponse("MissingRequestHeader", ex.getMessage());
    }

}
