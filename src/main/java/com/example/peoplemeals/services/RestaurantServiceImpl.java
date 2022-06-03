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
        Restaurant restaurantInDB = checkElementPresence(restaurantId);
        restaurantRepository.delete(restaurantInDB);
    }
    @Override
    public RestaurantDTO update(Long restaurantId, RestaurantDTO restaurantDTO) {
        Restaurant restaurantInDB = checkElementPresence(restaurantId);
        Restaurant restaurantUpdated = restaurantMapper.restaurantDTOToRestaurant(restaurantDTO);
        restaurantUpdated.setId(restaurantInDB.getId()); //Set correct ID from DB
        restaurantRepository.save(restaurantUpdated);
        return restaurantMapper.restaurantToRestaurantDTO(restaurantUpdated);
    }
    private Restaurant checkElementPresence(Long restaurantId) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (restaurantOptional.isEmpty()) {
            throw new IllegalArgumentException("No Restaurant with this ID was found in Database");
        }
        return restaurantOptional.get();
    }
}
