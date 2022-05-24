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

    @Column(name = "openingtime")
    private LocalDateTime openingTime;

    @Column(name = "closingtime")
    private LocalDateTime closingTime;

    @Column(name = "staffrestday")
    private DayOfWeek staffRestDay;
}
