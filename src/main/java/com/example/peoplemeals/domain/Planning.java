package com.example.peoplemeals.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.persistence.*;
import java.time.DayOfWeek;

@Entity
@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class Planning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day_of_week", length = 10)
    private DayOfWeek dayOfWeek;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;
}
