package com.example.peoplemeals.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@With
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    @NotBlank
    private String name;

    @Column(name = "recipe_url")
    private String recipeUrl;
}
