package com.example.peoplemeals.repositories;

import com.example.peoplemeals.domain.Restaurant;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends PeopleMealRepository<Restaurant, Long> {


}
