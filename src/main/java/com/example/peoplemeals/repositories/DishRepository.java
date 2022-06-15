package com.example.peoplemeals.repositories;

import com.example.peoplemeals.domain.Dish;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends PeopleMealRepository<Dish, Long> {

//    @Query("SELECT COUNT(id) from Dish where id = :id")
//    Long countById()
}
