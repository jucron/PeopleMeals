package com.example.peoplemeals.repositories;

import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.domain.Planning;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public interface PlanningRepository extends PeopleMealRepository<Planning, Long> {

    @Query("SELECT p.id FROM Planning p WHERE p.dish.id = :dishId and p.person.id = :personId and p.restaurant.id = :restaurantId and p.dayOfWeek = :dayOfWeek")
    Optional<Long> findPlanningIdByFields(Long dishId, Long personId, Long restaurantId, DayOfWeek dayOfWeek);

    default long findPlanningIdRequiredByFields(Long dishId, Long personId, Long restaurantId, DayOfWeek dayOfWeek) {
        return findPlanningIdByFields(dishId, personId, restaurantId, dayOfWeek)
                .orElseThrow(() -> new NoSuchElementException("No Plannings with these parameters were found in Database"));
    }

    @Query("SELECT COUNT(p.id) FROM Planning p WHERE p.person.id = :personId and p.dayOfWeek = :dayOfWeek")
    int countPlanningByPersonIdAndDayOfWeek(Long personId, DayOfWeek dayOfWeek);

    default boolean validateNoPlanForThisPersonInDayOfWeek(Long personId, DayOfWeek dayOfWeek) {
        //Only 1 meal per day per person
        if (countPlanningByPersonIdAndDayOfWeek(personId, dayOfWeek) > 0) {
            throw new RuntimeException("This Person already have a Planning for this DayOfWeek");
        }
        return true;
    }

    @Query("SELECT COUNT(p.id) FROM Planning p WHERE p.restaurant.id = :restaurantId and p.dayOfWeek = :dayOfWeek")
    int countPlanningByRestaurantIdAndDayOfWeek(Long restaurantId, DayOfWeek dayOfWeek);

    default boolean validateLessThan15RestaurantsInDayOfWeek(Long restaurantId, DayOfWeek dayOfWeek) {
        if (countPlanningByRestaurantIdAndDayOfWeek(restaurantId, dayOfWeek) < 15) {
            return true;
        } else {
            throw new RuntimeException("This restaurant have already the maximum number of dishes");
        }
    }

    @Query("SELECT p.person FROM Planning p WHERE p.restaurant.id = :restaurantId and p.dayOfWeek = :dayOfWeek")
    List<Person> findPersonsByRestaurantAndDayOfWeek(Long restaurantId, DayOfWeek dayOfWeek);

    @Query("SELECT p.person FROM Planning p WHERE p.dish.id = :dishId and p.dayOfWeek = :dayOfWeek")
    List<Person> findPersonsByDishAndDayOfWeek(Long dishId, DayOfWeek dayOfWeek);

    @Query("SELECT p.person.id FROM Planning p WHERE p.dayOfWeek = :dayOfWeek")
    List<Long> findPersonIDsByDayOfWeek(DayOfWeek dayOfWeek);

}

