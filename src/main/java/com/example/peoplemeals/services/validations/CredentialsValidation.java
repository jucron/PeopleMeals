package com.example.peoplemeals.services.validations;

import com.example.peoplemeals.domain.security.Credentials;
import com.example.peoplemeals.domain.security.Role;

public interface CredentialsValidation {

    Role validateRoleFormat(String role);

    boolean validateNoSameUsernameInDatabase(String username);

    String validateCredentialsActive(Credentials credentials);
}
