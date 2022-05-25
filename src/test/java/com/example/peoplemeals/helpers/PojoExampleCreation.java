package com.example.peoplemeals.helpers;

import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.domain.Planning;
import com.example.peoplemeals.domain.Restaurant;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public class PojoExampleCreation {

    public static Dish createDishExample () {
        return new Dish()
                .withId(1L)
                .withName("dishName_example")
                .withRecipeUrl("url_example");
    }
    public static Person createPersonExample () {
        return new Person()
                .withId(10L)
                .withFullName("fullName_example")
                .withUsername("username_example")
                .withMobile("mobile_example")
                .withTelephone("telephone_example")
                .withFiscal("fiscal_example");
    }
    public static Planning createPlanningExample () {
        return new Planning()
                .withId(50L)
                .withDayOfWeek(DayOfWeek.MONDAY)
                .withDish(createDishExample())
                .withPerson(createPersonExample());
    }
    public static Restaurant createRestaurantExample () {
        return new Restaurant()
                .withId(100L)
                .withName("restaurantName_example")
                .withOpeningTime(LocalTime.of(8,0))
                .withClosingTime(LocalTime.of(20,0))
                .withStaffRestDay(DayOfWeek.SUNDAY)
                .withDishes(Set.of(createDishExample()));
    }
}
