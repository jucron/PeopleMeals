package com.example.peoplemeals.api.v1.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {

    private UUID uuid;
    private String name;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private DayOfWeek staffRestDay;
    private Set<DishDTO> dishDTOS;

    private String creatorUsername;
    private String lastModifierUsername;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
