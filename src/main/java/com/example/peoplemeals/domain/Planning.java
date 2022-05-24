package com.example.peoplemeals.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.DayOfWeek;

@Entity
@Data
public class Planning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;
}
