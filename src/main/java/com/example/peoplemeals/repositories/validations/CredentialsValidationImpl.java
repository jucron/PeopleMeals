package com.example.peoplemeals.repositories.validations;

import com.example.peoplemeals.domain.security.Credentials;
import com.example.peoplemeals.domain.security.Role;
import com.example.peoplemeals.repositories.CredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CredentialsValidationImpl implements CredentialsValidation {
    private final CredentialsRepository credentialsRepository;

    @Override
    public boolean validateNoSameUsernameInDatabase(String username) {
        if (credentialsRepository.findByUsername(username).isPresent()) {
            throw new ValidationFailedException("This username is already being used");
        }
        return true;
    }

    @Override
    public String validateCredentialsActive(Credentials credentials) {
        if (credentials.getDeactivationDate() == null) {
            return credentials.getRole().role;
        }
        return Role.DEACTIVATED.role;
    }
}
