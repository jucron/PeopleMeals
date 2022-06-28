package com.example.peoplemeals.services.validations;

import com.example.peoplemeals.domain.security.Credentials;

public interface CredentialsValidation {

    boolean validateNoSameUsernameInDatabase(String username);

    String validateCredentialsActive(Credentials credentials);
}
