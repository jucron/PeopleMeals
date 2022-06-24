package com.example.peoplemeals.domain.security;

public enum Role {
    USER("user"),
    ADMIN("admin");

    public final String role;

    Role(String role) {
        this.role = role;
    }
}
