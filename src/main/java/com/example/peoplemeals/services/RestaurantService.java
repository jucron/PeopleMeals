package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.model.RestaurantDTO;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;

public interface RestaurantService {
    EntityDTOList<RestaurantDTO> getAll();

    RestaurantDTO get(String restaurantUuid);

    RestaurantDTO add(RestaurantDTO restaurantDTO);

    void remove(String restaurantUuid);

    RestaurantDTO update(String restaurantUuid, RestaurantDTO restaurantDTO);
}
