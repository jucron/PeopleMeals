package com.example.peoplemeals.controllers;

import com.example.peoplemeals.helpers.ValidationFailedException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    public static String GENERIC_EXCEPTION_MESSAGE = "An internal error has occurred, please contact administrator";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        return new ResponseEntity<>(new ExceptionMessage(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(
            NoSuchElementException ex, WebRequest request) {
        return new ResponseEntity<>(new ExceptionMessage(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<Object> handleValidationFailedException(
            ValidationFailedException ex, WebRequest request) {
        return new ResponseEntity<>(new ExceptionMessage(ex), HttpStatus.BAD_REQUEST);
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

