package com.example.peoplemeals.repositories.validations;

import java.time.DayOfWeek;

public interface PlanningValidation {

    boolean validateNoPlanForThisPersonInDayOfWeek(Long personId, DayOfWeek dayOfWeek);

    boolean validateLessThan15RestaurantsInDayOfWeek(Long restaurantId, DayOfWeek dayOfWeek);
}
