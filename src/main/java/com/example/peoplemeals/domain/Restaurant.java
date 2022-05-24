package com.example.peoplemeals.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Entity
@Data
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "opening_time")
    private LocalDateTime openingTime;

    @Column(name = "closing_time")
    private LocalDateTime closingTime;

    @Column(name = "staff_rest_day")
    private DayOfWeek staffRestDay;
}
