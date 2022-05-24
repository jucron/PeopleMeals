package com.example.peoplemeals.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "recipe_url")
    private String recipeUrl;
}
