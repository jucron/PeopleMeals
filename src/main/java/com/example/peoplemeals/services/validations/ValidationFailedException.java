package com.example.peoplemeals.services.validations;

public class ValidationFailedException extends RuntimeException {
    public ValidationFailedException(String s) {
        super(s);
    }
}
