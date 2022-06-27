package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.model.forms.UserForm;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;
import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.domain.security.Credentials;
import com.example.peoplemeals.repositories.CredentialsRepository;
import com.example.peoplemeals.repositories.PersonRepository;
import com.example.peoplemeals.repositories.validations.CredentialsValidation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static com.example.peoplemeals.domain.security.Role.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CredentialsServiceImplTest {
    private CredentialsService credentialsService;
    private UserDetailsService userDetailsService;

    @Mock
    private CredentialsRepository credentialsRepository;
    @Mock
    private CredentialsValidation credentialsValidation;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Captor
    private ArgumentCaptor<Credentials> argumentCaptor;

    @BeforeEach
    public void setUpForAll() {
        //Instantiate service class
        credentialsService = new CredentialsServiceImpl(credentialsRepository, credentialsValidation, personRepository, passwordEncoder);
        userDetailsService = new CredentialsServiceImpl(credentialsRepository, credentialsValidation, personRepository, passwordEncoder);
    }

    @Nested
    class SuccessfulServices {
        String username = "username_example";
        UserForm form = new UserForm()
                .withUsername(username).withPassword("pass").withRole(USER);
        String passEncoded = "pass_encoded";

        @Test
        void deactivateUser() {
            //given
            given(credentialsRepository.findRequiredByUsername(username)).willReturn(new Credentials());
            //when
            credentialsService.deactivateUser(username);
            //then
            verify(credentialsRepository).findRequiredByUsername(username);
            verify(credentialsRepository).save(argumentCaptor.capture());

            Credentials credentialsSaved = argumentCaptor.getValue();
            assertNotNull(credentialsSaved.getDeactivationDate());
        }

        @Test
        void getAll() {
            //given
            given(credentialsRepository.findAll()).willReturn(new ArrayList<>(List.of(new Credentials())));
            //when
            EntityDTOList<Credentials> list = credentialsService.getAll();
            //then
            verify(credentialsRepository).findAll();
            assertEquals("********", list.getEntityDTOList().iterator().next().getPassword());
        }

        @Test
        void loadUserByUsername() {
            //given
            Credentials credentials = new Credentials()
                    .withUsername(username).withPassword("pass");
            String roleExample = "role_example";
            given(credentialsRepository.findRequiredByUsername(username)).willReturn(credentials);
            given(credentialsValidation.validateCredentialsActive(credentials)).willReturn(roleExample);
            //when
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            //then
            verify(credentialsRepository).findRequiredByUsername(username);
            verify(credentialsValidation).validateCredentialsActive(any(Credentials.class));
            assertEquals(roleExample, userDetails.getAuthorities().iterator().next().getAuthority());
        }

        @Nested
        class CreateUser {
            @BeforeEach
            void commonStubs() {
                //given
                given(passwordEncoder.encode(anyString())).willReturn(passEncoded);
            }

            @Test
            void createUserWithoutPerson() {
                //when
                form.setPersonUuid(null);
                credentialsService.createUser(form);
                //then
                verify(credentialsRepository).save(argumentCaptor.capture());

                Credentials credentialsSaved = argumentCaptor.getValue();
                assertEquals(passEncoded, credentialsSaved.getPassword()); //ensure password is encoded
                assertNull(credentialsSaved.getPerson());

                verify(personRepository, times(0)).findRequiredByUuid(any());
            }

            @Test
            void createUserWithPerson() {
                //given
                form.setPersonUuid("some-uuid");
                given(personRepository.findRequiredByUuid(anyString())).willReturn(new Person());
                //when
                credentialsService.createUser(form);
                //then
                verify(credentialsRepository).save(argumentCaptor.capture());

                Credentials credentialsSaved = argumentCaptor.getValue();
                assertEquals(passEncoded, credentialsSaved.getPassword()); //ensure password is encoded
                assertNotNull(credentialsSaved.getPerson());

                verify(personRepository, times(1)).findRequiredByUuid(any());
            }

            @AfterEach
            void commonChecks() {
                verify(credentialsValidation).validateNoSameUsernameInDatabase(form.getUsername());
                verify(passwordEncoder).encode(form.getPassword());
            }
        }
    }

    @Nested
    class FailedServices {
        @Test
        void createUser_emptyBody() {
            assertThrows(NullPointerException.class, () -> credentialsService.createUser(null));
        }

        @Nested
        class UsernameNotInDB {
            String username = "username";

            @BeforeEach
            void setUpStubs() {
                //given expected behavior
                given(credentialsRepository.findRequiredByUsername(username)).willThrow(UsernameNotFoundException.class);
            }

            @Test
            void deactivateUser_usernameNotInDb() {
                //then
                assertThrows(UsernameNotFoundException.class, () -> credentialsService.deactivateUser(username));
            }

            @Test
            void loadUserByUsername_usernameNotInDb() {
                //then
                assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
            }
        }
    }
}