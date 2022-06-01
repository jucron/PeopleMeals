package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.model.RestaurantDTO;

public interface RestaurantService {
    RestaurantDTO add(RestaurantDTO restaurantDTO);

    void remove(Long restaurantId);

    RestaurantDTO update(Long restaurantId, RestaurantDTO restaurantDTO);
}
