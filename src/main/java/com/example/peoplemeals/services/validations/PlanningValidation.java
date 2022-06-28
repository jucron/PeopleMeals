package com.example.peoplemeals.services.validations;

import java.time.DayOfWeek;

public interface PlanningValidation {

    boolean validateNoPlanForThisPersonInDayOfWeek(Long personId, DayOfWeek dayOfWeek);

    boolean validateLessThan15RestaurantsInDayOfWeek(Long restaurantId, DayOfWeek dayOfWeek);
}
