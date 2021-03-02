package com.epam.esm.controller.exception;

import com.epam.esm.model.service.exception.IncorrectArgumentException;
import com.epam.esm.model.service.exception.NotExistEntityException;
import com.epam.esm.model.service.exception.ServiceException;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class RestExceptionHandler {

    private final MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();
    private final AtomicInteger atomicInteger = new AtomicInteger();

    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ServiceException.class)
    private ResponseEntity<ErrorResponse> handleServiceException(ServiceException exception) {
        String message = messageSource.getMessage("service", null, locale);
        return ResponseEntity.status(BAD_REQUEST).body(buildErrorResponse(BAD_REQUEST,
                message + " " + exception.getLocalizedMessage()));
    }

    @ExceptionHandler(NotExistEntityException.class)
    private ResponseEntity<ErrorResponse> handleNotExistException(NotExistEntityException exception) {
        String message = messageSource.getMessage("notExistEntity", null, locale);
        return ResponseEntity.status(NOT_FOUND).body(buildErrorResponse(NOT_FOUND,
                message + " " + exception.getLocalizedMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        String internationale = messageSource.getMessage("runtime", null, locale);
        String message = exception.getClass().getName() + " : " + exception.getMessage();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(buildErrorResponse(INTERNAL_SERVER_ERROR,
                internationale + " " + message));
    }

    @ExceptionHandler(IncorrectArgumentException.class)
    private ResponseEntity<ErrorResponse> handleIncorrectArgumentException(IncorrectArgumentException exception) {
        String internationale = messageSource.getMessage("incorrectArgument", null, locale);
        String message = exception.getClass().getName() + " : " + exception.getMessage();
        return ResponseEntity.status(BAD_REQUEST).body(buildErrorResponse(BAD_REQUEST,
                internationale + " " + message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        StringBuilder builder = new StringBuilder();
        exception.getBindingResult().getAllErrors().forEach(e -> {
            String fieldName = ((FieldError) e).getField();
            String errorMessage = e.getDefaultMessage();
            builder.append(fieldName).append(" ").append(errorMessage).append("; ");
        });
        return ResponseEntity.status(BAD_REQUEST).body(buildErrorResponse(BAD_REQUEST, builder.toString()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    private ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        String internationale = messageSource.getMessage("usernameNotFoundException", null, locale);
        String message = exception.getClass().getName() + " : " + exception.getMessage();
        return ResponseEntity.status(NOT_FOUND).body(buildErrorResponse(NOT_FOUND,
                internationale + " " + message));
    }

    private ErrorResponse buildErrorResponse(HttpStatus status, String message) {
        return new ErrorResponse(message, status.value() * 100 + atomicInteger.incrementAndGet());
    }
}
