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
    class validateLessThan3PlansForThisPersonInDayOfWeek {
        long personId = 1L;

        @Test
        void isValid() {
            //given
            given(planningRepository.countPlanningByPersonIdAndDayOfWeek(personId, dayOfWeek)).willReturn(0);
            given(planningRepository.countPlanningByPersonIdAndDayOfWeek(personId + 1, dayOfWeek)).willReturn(1);
            given(planningRepository.countPlanningByPersonIdAndDayOfWeek(personId + 2, dayOfWeek)).willReturn(2);
            //then
            assertTrue(planningValidation.validateLessThan3PlansForThisPersonInDayOfWeek(personId, dayOfWeek));
            assertTrue(planningValidation.validateLessThan3PlansForThisPersonInDayOfWeek(personId + 1, dayOfWeek));
            assertTrue(planningValidation.validateLessThan3PlansForThisPersonInDayOfWeek(personId + 2, dayOfWeek));
        }

        @Test
        void isNotValid() {
            //given
            given(planningRepository.countPlanningByPersonIdAndDayOfWeek(personId, dayOfWeek)).willReturn(3);
            given(planningRepository.countPlanningByPersonIdAndDayOfWeek(personId + 1, dayOfWeek)).willReturn(4);
            given(planningRepository.countPlanningByPersonIdAndDayOfWeek(personId + 2, dayOfWeek)).willReturn(20);
            //then
            assertThrows(ValidationFailedException.class, () -> planningValidation.validateLessThan3PlansForThisPersonInDayOfWeek(personId, dayOfWeek));
            assertThrows(ValidationFailedException.class, () -> planningValidation.validateLessThan3PlansForThisPersonInDayOfWeek(personId + 1, dayOfWeek));
            assertThrows(ValidationFailedException.class, () -> planningValidation.validateLessThan3PlansForThisPersonInDayOfWeek(personId + 2, dayOfWeek));
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