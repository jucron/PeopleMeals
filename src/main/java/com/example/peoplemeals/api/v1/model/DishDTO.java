package com.example.peoplemeals.api.v1.model;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@With
@AllArgsConstructor
@NoArgsConstructor
public class DishDTO {

    private UUID uuid;
    private String name;
    private String recipeUrl;

}
