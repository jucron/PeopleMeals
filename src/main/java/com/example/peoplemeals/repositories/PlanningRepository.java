package com.example.peoplemeals.repositories;

import com.example.peoplemeals.domain.Planning;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanningRepository extends PeopleMealRepository<Planning, Long> {

    @Query(value = "SELECT p.id FROM Planning p WHERE p.dish_id = ?1 and p.person_id = ?2 and p.restaurant_id = ?3", nativeQuery = true)
    Optional<Long> findPlanningIdByDishIdPersonIdRestaurantId(Long dishId, Long personId, Long restaurantId);

    default long findPlanningIdRequiredByDishIdPersonIdRestaurantId(
            Long dishId, Long personId, Long restaurantId) {
        return findPlanningIdByDishIdPersonIdRestaurantId(dishId, personId, restaurantId)
                .orElseThrow(()->new IllegalArgumentException("This planning already exists in Database"));
    }

    @Query(value = "SELECT COUNT(p.id) FROM Planning p WHERE p.restaurant_id = :restaurantId and p.day_of_week = :dayOfWeek"
    ,nativeQuery = true)
    int countPlanningByRestaurantIdAndDayOfWeek(Long restaurantId, String dayOfWeek);

    default boolean validateLessThan15RestaurantsInDayOfWeek(
            Long restaurantId, String dayOfWeek) {
        if (countPlanningByRestaurantIdAndDayOfWeek(restaurantId,dayOfWeek) < 15) {
            return true;
        } else {
            throw new IllegalArgumentException(
                    "This restaurant have already the maximum number of dishes");
        }
    }

}

