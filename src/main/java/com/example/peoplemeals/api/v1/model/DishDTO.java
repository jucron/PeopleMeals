package com.example.peoplemeals.api.v1.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class DishDTO {

    private Long id;
    private String name;
    private String recipeUrl;

}
