package com.example.peoplemeals.repositories;

import com.example.peoplemeals.domain.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends PeopleMealRepository<Restaurant, Long> {

    @Query("SELECT r.maxNumberOfMealsPerDay FROM Restaurant r WHERE r.id = :restaurantId")
    Optional<Integer> findMaxNumberOfMealsPerDayByRestaurantId(Long restaurantId);

    default int findMaxNumberOfMealsPerDayRequiredByRestaurantId(Long restaurantId) {
        return findMaxNumberOfMealsPerDayByRestaurantId(restaurantId).orElseThrow(() ->
                new NoSuchElementException("The maximum number of meals per day was not defined in this Restaurant"));
    }
}
