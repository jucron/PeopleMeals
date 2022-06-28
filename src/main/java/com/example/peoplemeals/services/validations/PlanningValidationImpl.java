package com.example.peoplemeals.services.validations;

import com.example.peoplemeals.repositories.PlanningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
@RequiredArgsConstructor
public class PlanningValidationImpl implements PlanningValidation {
    private final PlanningRepository planningRepository;

    @Override
    public boolean validateLessThan3PlansForThisPersonInDayOfWeek(Long personId, DayOfWeek dayOfWeek) {
        //Maximum of 3 meals per day per person
        if (planningRepository.countPlanningByPersonIdAndDayOfWeek(personId, dayOfWeek) >= 3) {
            throw new ValidationFailedException("This Person already have the maximum Plannings for this DayOfWeek");
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
