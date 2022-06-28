package com.example.peoplemeals.services.validations;

import com.example.peoplemeals.repositories.PlanningRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.transaction.Transactional;
import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PlanningValidationImplTest {
    DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
    private PlanningValidation planningValidation;
    @Mock
    private PlanningRepository planningRepository;

    @BeforeEach
    void setUp() {
        planningValidation = new PlanningValidationImpl(planningRepository);
    }

    @Nested
    class validateNoPlanForThisPersonInDayOfWeek {
        long personId = 1L;

        @Test
        void isValid() {
            //given
            given(planningRepository.countPlanningByPersonIdAndDayOfWeek(personId, dayOfWeek)).willReturn(0);
            //then
            assertTrue(planningValidation.validateNoPlanForThisPersonInDayOfWeek(personId, dayOfWeek));
        }

        @Test
        void isNotValid() {
            //given
            given(planningRepository.countPlanningByPersonIdAndDayOfWeek(personId, dayOfWeek)).willReturn(1);
            //then
            assertThrows(ValidationFailedException.class, () -> planningValidation.validateNoPlanForThisPersonInDayOfWeek(personId, dayOfWeek));
        }
    }

    @Nested
    class validateLessThan15RestaurantsInDayOfWeek {
        long restaurantId = 1L;

        @Test
        void isValid() {
            //given
            int countExpected = 10;
            given(planningRepository.countPlanningByRestaurantIdAndDayOfWeek(restaurantId, dayOfWeek)).willReturn(countExpected);
            //then
            assertTrue(planningValidation.validateLessThan15RestaurantsInDayOfWeek(restaurantId, dayOfWeek));
        }

        @Test
        @Transactional
        void isNotValid() {
            //given
            int countExpected = 15;
            given(planningRepository.countPlanningByRestaurantIdAndDayOfWeek(restaurantId, dayOfWeek)).willReturn(countExpected);
            //then
            assertThrows(ValidationFailedException.class, () -> planningValidation.validateLessThan15RestaurantsInDayOfWeek(restaurantId, dayOfWeek));
        }
    }
}