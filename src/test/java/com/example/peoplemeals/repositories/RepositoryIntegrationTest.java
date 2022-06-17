package com.example.peoplemeals.repositories;

import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.domain.Planning;
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

    @Autowired
    private com.example.peoplemeals.repositories.PlanningRepository planningRepository;

    private final UUID RANDOM_UUID = UUID.randomUUID();
    private Dish dishToBeTested;
    private Planning planningToBeTested;
    private boolean bootstrapData = false;

    @BeforeEach
    void setUpData() {
        if (!bootstrapData) {
            dishToBeTested = dishRepository.save(PojoExampleCreation.createDishExample(1));
            dishRepository.save(PojoExampleCreation.createDishExample(2));

            Planning planning = PojoExampleCreation.createPlanningExample(3);

            planningToBeTested =planningRepository.save();

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
    class findIdByUuid {
        @Test
        void findIdByUuidExists() {
            Optional<ProjectId> dishIdOptional = dishRepository.findIdByUuid(dishToBeTested.getUuid());
            assertTrue(dishIdOptional.isPresent());
            assertEquals(dishToBeTested.getId(), dishIdOptional.get().getId());
        }

        @Test
        void findIdByUuidNotInDB() {
            Optional<ProjectId> dishIdOptional = dishRepository.findIdByUuid(RANDOM_UUID);
            assertTrue(dishIdOptional.isEmpty());
        }
    }

    @Nested
    class findIdRequiredByUuid {
        @Test
        void findIdRequiredByUuidExists() {
            long dishId = dishRepository.findIdRequiredByUuid(dishToBeTested.getUuid().toString());
            assertEquals(dishToBeTested.getId(),dishId);
        }

        @Test
        void findIdRequiredByUuidNotInDB() {
            assertThrows(NoSuchElementException.class,()->dishRepository.findIdRequiredByUuid(RANDOM_UUID.toString()));
        }

        @Test
        void findIdRequiredByUuidInWrongFormat() {
            assertThrows(IllegalArgumentException.class,()->UUID.fromString("some_wrong_format")); //nested exception catch by Spring
            assertThrows(NestedRuntimeException.class,()->dishRepository.findIdRequiredByUuid("some_wrong_format"));
        }
    }
    @Nested
    class PlanningRepository {
        @Test
        void findPlanningIdByDishIdPersonIdRestaurantId() {
            System.out.println(planningToBeTested);
            long dishId = planningToBeTested.getDish().getId();
            long personId = planningToBeTested.getPerson().getId();
            long restaurantId = planningToBeTested.getRestaurant().getId();

            Optional<Long> planningIdFetched = planningRepository.findPlanningIdByDishIdPersonIdRestaurantId(dishId,personId,restaurantId);
            //then
            assertTrue(planningIdFetched.isPresent());
            assertEquals(planningToBeTested.getId(),planningIdFetched.get());
        }

    }
}