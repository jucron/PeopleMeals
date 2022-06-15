package com.example.peoplemeals.api.v1.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.UUID;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class DishDTO {

    private UUID uuid;
    private String name;
    private String recipeUrl;

}
