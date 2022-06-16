package com.example.peoplemeals.repositories;

import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.helpers.PojoExampleCreation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.NestedRuntimeException;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RepositoryIntegrationTest {


    @Autowired
    private DishRepository dishRepository;

    private final UUID RANDOM_UUID = UUID.randomUUID();
    private Dish dishToBeTested;
    private boolean bootstrapData = false;

    @BeforeEach
    void setUpData() {
        if (!bootstrapData) {
            dishToBeTested = dishRepository.save(PojoExampleCreation.createDishExample(1));
            dishRepository.save(PojoExampleCreation.createDishExample(2));
            bootstrapData = true;
        }
    }

    @Nested
    class findByUuid {
        @Test
        void findByUuidExists() {
            Optional<Dish> dishOptionalFetched = dishRepository.findByUuid(dishToBeTested.getUuid());
            assertTrue(dishOptionalFetched.isPresent());
            assertEquals(dishToBeTested.getId(),dishOptionalFetched.get().getId());
            assertEquals(dishToBeTested.getUuid(),dishOptionalFetched.get().getUuid());
        }

        @Test
        void findByUuidNotInDB() {
            Optional<Dish> dishOptionalFetched = dishRepository.findByUuid(RANDOM_UUID);
            assertTrue(dishOptionalFetched.isEmpty());
        }

    }
    @Nested
    class findRequiredByUuid {
        @Test
        void findRequiredByUuidExists() {
            Dish dishFetched = dishRepository.findRequiredByUuid(dishToBeTested.getUuid().toString());
            assertEquals(dishToBeTested.getId(),dishFetched.getId());
            assertEquals(dishToBeTested.getUuid(),dishFetched.getUuid());
        }

        @Test
        void findRequiredByUuidNotInDB() {
            assertThrows(NoSuchElementException.class,()->dishRepository.findRequiredByUuid(RANDOM_UUID.toString()));
        }

        @Test
        void findRequiredByUuidInWrongFormat() {
            assertThrows(IllegalArgumentException.class,()->UUID.fromString("some_wrong_format")); //nested exception catch by Spring
            assertThrows(NestedRuntimeException.class,()->dishRepository.findRequiredByUuid("some_wrong_format"));
        }
    }

    @Nested
    class countByUuid {
        @Test
        void countByUuidExists() {
            int count = dishRepository.countByUuid(dishToBeTested.getUuid());
            assertEquals(1,count);
        }

        @Test
        void countByUuidNotInDB() {
            int count = dishRepository.countByUuid(RANDOM_UUID);
            assertEquals(0,count);
        }
    }
    @Nested
    class isPresentRequiredByUuid {
        @Test
        void isPresentRequiredByUuidExists() {
            boolean check = dishRepository.isPresentRequiredByUuid(dishToBeTested.getUuid().toString());
            assertTrue(check);
        }

        @Test
        void isPresentRequiredByUuidNotInDB() {
            assertThrows(NoSuchElementException.class,()->dishRepository.isPresentRequiredByUuid(RANDOM_UUID.toString()));
        }
        @Test
        void isPresentRequiredByUuiWrongFormat() {
            assertThrows(NestedRuntimeException.class,()->dishRepository.isPresentRequiredByUuid("some_wrong_format"));
        }
    }

}