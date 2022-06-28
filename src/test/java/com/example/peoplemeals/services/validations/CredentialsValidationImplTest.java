package com.example.peoplemeals.services.validations;

import com.example.peoplemeals.domain.security.Credentials;
import com.example.peoplemeals.domain.security.Role;
import com.example.peoplemeals.repositories.CredentialsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CredentialsValidationImplTest {
    private CredentialsValidation credentialsValidation;
    @Mock
    private CredentialsRepository credentialsRepository;

    @BeforeEach
    void setUp() {
        credentialsValidation = new CredentialsValidationImpl(credentialsRepository);
    }

    @org.junit.jupiter.api.Nested
    class validateNoSameUsernameInDatabase {
        String username = "username";

        @Test
        void isValid() {
            //given
            given(credentialsRepository.findByUsername(username)).willReturn(Optional.empty());
            //when
            assertTrue(credentialsValidation.validateNoSameUsernameInDatabase(username));
        }

        @Test
        void isNotValid() {
            //given
            given(credentialsRepository.findByUsername(username)).willReturn(Optional.of(new Credentials()));
            //when
            assertThrows(ValidationFailedException.class, () ->
                    credentialsValidation.validateNoSameUsernameInDatabase(username));
        }
    }

    @org.junit.jupiter.api.Nested
    class validateCredentialsActive {
        Credentials credentials = new Credentials().withRole(Role.USER);

        @Test
        void isValid() {
            //given
            credentials.setDeactivationDate(null);
            //when
            assertEquals(credentials.getRole().role,
                    credentialsValidation.validateCredentialsActive(credentials));
        }

        @Test
        void isNotValid() {
            //given
            credentials.setDeactivationDate(LocalDateTime.now());
            //when
            assertEquals(Role.DEACTIVATED.role,
                    credentialsValidation.validateCredentialsActive(credentials));
        }
    }

}