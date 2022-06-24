package com.example.peoplemeals.domain;

import com.example.peoplemeals.domain.security.Credentials;
import com.example.peoplemeals.repositories.CredentialsRepository;
import com.example.peoplemeals.repositories.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class PersonAuditTest {

    @Autowired
    private CredentialsRepository credentialsRepository;
    @Autowired
    private PersonRepository personRepository;

    @Test
    void auditFields() {
        Person person = personRepository.save(new Person().withUuid(UUID.randomUUID()));

        Credentials credentials = credentialsRepository.save(new Credentials()
                        .withUsername("john")
                        .withPassword("123"))
                .withPerson(person);
        System.out.println(personRepository.findByUuid(person.getUuid()));
    }

}