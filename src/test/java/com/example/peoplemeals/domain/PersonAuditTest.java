package com.example.peoplemeals.domain;

import com.example.peoplemeals.config.PersistenceConfig;
import com.example.peoplemeals.repositories.PersonRepository;
import com.example.peoplemeals.repositories.RestaurantRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import(PersistenceConfig.class)
@WithMockUser(username = "username_example")
class PersonAuditTest {

    UUID uuid = UUID.randomUUID();
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Nested
    class auditFieldsInPerson {
        @Test
        void creatingEntity() {
            Person person = personRepository.save(new Person().withUuid(uuid));

            assertNotNull(person.getCreatedDate());
            assertNotNull(person.getCreatorUsername());
            assertNotNull(person.getLastModifiedDate());
            assertNotNull(person.getLastModifierUsername());
        }
    }

    @Nested
    class auditFieldsInRestaurant {
        @Test
        void creatingEntity() {
            Restaurant restaurant = restaurantRepository.save(new Restaurant().withUuid(UUID.randomUUID()));
            assertNotNull(restaurant.getCreatedDate());
            assertNotNull(restaurant.getCreatorUsername());
            assertNotNull(restaurant.getLastModifiedDate());
            assertNotNull(restaurant.getLastModifierUsername());
        }
    }
}