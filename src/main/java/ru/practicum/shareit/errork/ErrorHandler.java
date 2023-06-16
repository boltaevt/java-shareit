package ru.practicum.shareit.errork;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    private final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler({
            ItemNotFoundException.class,
            UserNotFoundException.class,
            AccessDeniedException.class,
            BookingNotFoundException.class,
            ItemRequestNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse itemNotFound(final RuntimeException e) {
        logger.info("item not found: {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationError(final MethodArgumentNotValidException e) {
        logger.info("validation failed: {}", e.getMessage(), e);
        return new ErrorResponse(e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getField)
                .collect(Collectors.toList())
                .toString());
    }

    @ExceptionHandler({ItemNotAvailableException.class, BookingStatusChangeDeniedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse itemUnavailableError(final RuntimeException e) {
        logger.info("item unavailable error: {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse conversionError(final MethodArgumentTypeMismatchException e) {
        logger.info("conversion error: {}", e.getMessage(), e);
        return new ErrorResponse("Unknown state: " + Objects.requireNonNull(e.getValue()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse internalError(final Throwable t) {
        logger.info("internal error: {}", t.getMessage(), t);
        return new ErrorResponse(t.getMessage());
    }
}