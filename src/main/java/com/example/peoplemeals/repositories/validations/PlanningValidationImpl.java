package com.example.peoplemeals.repositories.validations;

import com.example.peoplemeals.repositories.PlanningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
@RequiredArgsConstructor
public class PlanningValidationImpl implements PlanningValidation {
    private final PlanningRepository planningRepository;

    @Override
    public boolean validateNoPlanForThisPersonInDayOfWeek(Long personId, DayOfWeek dayOfWeek) {
        //Only 1 meal per day per person
        if (planningRepository.countPlanningByPersonIdAndDayOfWeek(personId, dayOfWeek) > 0) {
            throw new ValidationFailedException("This Person already have a Planning for this DayOfWeek");
        }
        return true;
    }

    @Override
    public boolean validateLessThan15RestaurantsInDayOfWeek(Long restaurantId, DayOfWeek dayOfWeek) {
        if (planningRepository.countPlanningByRestaurantIdAndDayOfWeek(restaurantId, dayOfWeek) < 15) {
            return true;
        } else {
            throw new ValidationFailedException("This restaurant have already the maximum number of dishes");
        }
    }
}
