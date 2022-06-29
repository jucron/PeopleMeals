package com.example.peoplemeals.domain;

import com.example.peoplemeals.domain.audit.Auditor;
import com.example.peoplemeals.helpers.NoCoverageGenerated;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@NoCoverageGenerated
@Entity
@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Restaurant extends Auditor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    @NotBlank
    private String name;

    @Column(name = "opening_time")
    private LocalTime openingTime;

    @Column(name = "closing_time")
    private LocalTime closingTime;

    @Column(name = "staff_rest_day")
    private DayOfWeek staffRestDay;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "rt_restaurant_dish",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id"))
    private Set<Dish> dishes;

    @Min(value = 1, message = "Restaurants should have at least one Meal per day")
    @Max(value = 200, message = "Restaurants should have less than 200 Meal per day")
    private int maxNumberOfMealsPerDay;
}
