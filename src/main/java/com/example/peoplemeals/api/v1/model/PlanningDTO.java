package com.example.peoplemeals.api.v1.model;


import com.example.peoplemeals.helpers.NoCoverageGenerated;
import lombok.*;

import java.time.DayOfWeek;
import java.util.UUID;

@NoCoverageGenerated
@Getter
@Setter
@EqualsAndHashCode
@With
@AllArgsConstructor
@NoArgsConstructor
public class PlanningDTO {

    private UUID uuid;
    private DayOfWeek dayOfWeek;
    private DishDTO dishDTO;
    private PersonDTO personDTO;
    private RestaurantDTO restaurantDTO;

}
