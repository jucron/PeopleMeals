package com.example.peoplemeals.services.validations;

import java.time.DayOfWeek;

public interface PlanningValidation {

    boolean validateLessThan3PlansForThisPersonInDayOfWeek(Long personId, DayOfWeek dayOfWeek);

    boolean validateLessThanMaxNumberOfMealsPerDayInRestaurant(Long restaurantId, DayOfWeek dayOfWeek);
}
