package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.RestaurantMapper;
import com.example.peoplemeals.api.v1.model.RestaurantDTO;
import com.example.peoplemeals.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Override
    public RestaurantDTO add(RestaurantDTO restaurantDTO) {
        return null;
    }

    @Override
    public void remove(Long restaurantId) {

    }

    @Override
    public RestaurantDTO update(Long restaurantId, RestaurantDTO restaurantDTO) {
        return null;
    }
}
