package com.example.peoplemeals.repositories.validations;

public class ValidationFailedException extends RuntimeException {
    public ValidationFailedException(String s) {
        super(s);
    }
}
