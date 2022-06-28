package com.example.peoplemeals.controllers;

import com.example.peoplemeals.services.validations.ValidationFailedException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    public static String GENERIC_EXCEPTION_MESSAGE = "An internal error has occurred, please contact administrator";

    @ExceptionHandler({ValidationFailedException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> handleValidationFailedAndIllegalArgumentExceptions(
            RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(new ExceptionMessage(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(
            NoSuchElementException ex, WebRequest request) {
        return new ResponseEntity<>(new ExceptionMessage(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Object> handleSecurityException(
            SecurityException ex, WebRequest request) {
        return new ResponseEntity<>(new ExceptionMessage(ex),
                ex.getCause() != null ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {
        return new ResponseEntity<>(new ExceptionMessage(ex), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAnyException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>(new ExceptionMessage(GENERIC_EXCEPTION_MESSAGE), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

@Data
class ExceptionMessage {
    private String message;

    public ExceptionMessage(Exception ex) {
        this.message = ex.getMessage();
    }

    public ExceptionMessage(String message) {
        this.message = message;
    }
}

