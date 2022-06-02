package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.RestaurantMapper;
import com.example.peoplemeals.api.v1.model.RestaurantDTO;
import com.example.peoplemeals.domain.Restaurant;
import com.example.peoplemeals.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Override
    public RestaurantDTO add(RestaurantDTO restaurantDTO) {
        Restaurant restaurantToBeSaved = restaurantMapper.restaurantDTOToRestaurant(restaurantDTO);
        restaurantToBeSaved.setId(null); //must remove ID to perform auto-generate
        restaurantRepository.save(restaurantToBeSaved);
        return restaurantMapper.restaurantToRestaurantDTO(restaurantToBeSaved);
    }
    @Override
    public void remove(Long restaurantId) {
        try {
            restaurantRepository.deleteById(restaurantId);
        } catch (IllegalArgumentException e) {
            //todo: How to handle this?
        }
    }
    @Override
    public RestaurantDTO update(Long restaurantId, RestaurantDTO restaurantDTO) {
        try {
            Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
            if (restaurantOptional.isEmpty()) {
                throw new IllegalArgumentException();
            }
            Restaurant restaurantUpdated = restaurantMapper.restaurantDTOToRestaurant(restaurantDTO);
            restaurantUpdated.setId(restaurantOptional.get().getId()); //Set correct ID from DB
            restaurantRepository.save(restaurantUpdated);
            return restaurantMapper.restaurantToRestaurantDTO(restaurantUpdated);
        } catch (IllegalArgumentException e) {
            //todo: How to handle this?
        }
        return null;
    }
}
