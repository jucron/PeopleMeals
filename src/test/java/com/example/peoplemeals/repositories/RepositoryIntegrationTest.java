package com.example.peoplemeals.repositories;

import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.helpers.PojoExampleCreation;
import org.junit.jupiter.api.BeforeEach;
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

    boolean bootstrapData = false;
    @Autowired
    private DishRepository dishRepository;
    private Dish dishToBeTested;

    @BeforeEach
    void setUp() {
        if (!bootstrapData) {
            dishToBeTested = dishRepository.save(PojoExampleCreation.createDishExample(1));
            dishRepository.save(PojoExampleCreation.createDishExample(2));
            bootstrapData = true;
        }
    }

    @Test
    void findByUuidExists() {
        Optional<Dish> dishOptionalFetched = dishRepository.findByUuid(dishToBeTested.getUuid());
        assertTrue(dishOptionalFetched.isPresent());
        assertEquals(dishToBeTested.getId(),dishOptionalFetched.get().getId());
        assertEquals(dishToBeTested.getUuid(),dishOptionalFetched.get().getUuid());
    }

    @Test
    void findByUuidNotInDB() {
        Optional<Dish> dishOptionalFetched = dishRepository.findByUuid(UUID.randomUUID());
        assertTrue(dishOptionalFetched.isEmpty());
    }

    @Test
    void findRequiredByUuidExists() {
        Dish dishFetched = dishRepository.findRequiredByUuid(dishToBeTested.getUuid().toString());
        assertEquals(dishToBeTested.getId(),dishFetched.getId());
        assertEquals(dishToBeTested.getUuid(),dishFetched.getUuid());
    }

    @Test
    void findRequiredByUuidNotInDB() {
        assertThrows(NoSuchElementException.class,()->dishRepository.findRequiredByUuid(UUID.randomUUID().toString()));
    }

    @Test
    void findRequiredByUuidInWrongFormat() {
        assertThrows(NestedRuntimeException.class,()->dishRepository.findRequiredByUuid("some_wrong_format"));
        assertThrows(IllegalArgumentException.class,()->UUID.fromString("some_wrong_format"));

    }
}