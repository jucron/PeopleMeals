package com.example.peoplemeals.services.validations;

public class DayOfWeekValidation {

    public static java.time.DayOfWeek validateDayOfWeek(String dayOfWeek) {
        try {
            return java.time.DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("DayOfWeek is not in valid format");
        }
    }
}
