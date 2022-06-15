package com.example.peoplemeals.repositories;

import com.example.peoplemeals.domain.Planning;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanningRepository extends PeopleMealRepository<Planning, Long> {

    @Query("SELECT COUNT(p.id) FROM Planning p where p.dish.id = :dishId and p.person.id = :personId and p.restaurant.id = :restaurantId")
    int findPlanningByDishIdAndPersonId(Long dishId, Long personId, Long restaurantId);
}
