package com.example.peoplemeals.domain;

import com.example.peoplemeals.helpers.NoCoverageGenerated;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.UUID;

@NoCoverageGenerated
@Entity
@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class Planning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    @NotNull
    @Column(name = "day_of_week", length = 10)
    private DayOfWeek dayOfWeek;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
