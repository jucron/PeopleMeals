package com.example.peoplemeals.api.v1.model;


import com.example.peoplemeals.domain.Dish;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {

    private Long id;
    private String name;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    private DayOfWeek staffRestDay;
    private Set<Dish> dishes;
}
