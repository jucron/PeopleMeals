package com.example.peoplemeals.helpers;

public class ValidationFailedException extends RuntimeException {
    public ValidationFailedException(String s) {
        super(s);
    }
}
