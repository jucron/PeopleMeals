package com.example.peoplemeals.api.v1.model;


import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.domain.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.DayOfWeek;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class PlanningDTO {

    private Long id;
    private DayOfWeek dayOfWeek;
    private Dish dish;
    private Person person;

}
