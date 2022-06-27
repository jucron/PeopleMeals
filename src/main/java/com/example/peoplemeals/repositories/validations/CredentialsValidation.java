package com.example.peoplemeals.repositories.validations;

import com.example.peoplemeals.domain.security.Credentials;

public interface CredentialsValidation {

    boolean validateNoSameUsernameInDatabase(String username);

    String validateCredentialsActive(Credentials credentials);
}
