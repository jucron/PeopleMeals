package com.example.peoplemeals.controllers;

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

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        return new ResponseEntity<>(new ExceptionMessage(ex), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({NullPointerException.class})
    public void handleNullPointerException() {
        throw new IllegalArgumentException();
    }
    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<Object> handleNoSuchElementException(
            NoSuchElementException ex, WebRequest request) {
        return new ResponseEntity<>(new ExceptionMessage(ex), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAnyException(
            Exception ex, WebRequest request) {
        String customMessage = "An internal error has occurred, please contact administrator";
        return new ResponseEntity<>(new ExceptionMessage(ex,customMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
@Data
class ExceptionMessage {
    private Exception ex;
    private String message;
    private String cause;

    public ExceptionMessage(Exception ex) {
        this.ex = ex;
        this.message = ex.getMessage();
        this.cause = String.valueOf(ex.getCause());
    }
    public ExceptionMessage(Exception ex, String message) {
        this.ex = ex;
        this.message = message;
        this.cause = String.valueOf(ex.getCause());
    }
}

