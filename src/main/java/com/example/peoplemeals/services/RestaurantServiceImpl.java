package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.RestaurantMapper;
import com.example.peoplemeals.api.v1.model.RestaurantDTO;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;
import com.example.peoplemeals.domain.Restaurant;
import com.example.peoplemeals.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Override
    public EntityDTOList<RestaurantDTO> getAll() {
        return new EntityDTOList<>(restaurantRepository.findAll().stream()
                .map(restaurantMapper::restaurantToRestaurantDTO)
                .collect(Collectors.toSet()));
    }

    @Override
    public RestaurantDTO get(String restaurantUuid) {
        return restaurantMapper.restaurantToRestaurantDTO(
                restaurantRepository.findRequiredByUuid(restaurantUuid));
    }

    @Override
    public RestaurantDTO add(RestaurantDTO restaurantDTO) {
        Restaurant restaurantToBeSaved = restaurantMapper.restaurantDTOToRestaurant(restaurantDTO);
        restaurantToBeSaved.setId(null); //must remove ID to perform auto-generate
        restaurantToBeSaved.setUuid(UUID.randomUUID());
        Restaurant restaurantSaved = restaurantRepository.save(restaurantToBeSaved);
        return restaurantMapper.restaurantToRestaurantDTO(restaurantSaved);
    }

    @Override
    public void remove(String restaurantUuid) {
        restaurantRepository.deleteById(restaurantRepository.findIdRequiredByUuid(restaurantUuid));
    }

    @Override
    public RestaurantDTO update(String restaurantUuid, RestaurantDTO restaurantDTO) {
        long idOfRestaurantInDB = restaurantRepository.findIdRequiredByUuid(restaurantUuid);
        //Get new Data from DTO; set ID and UUID from original, to replace existing data when saved
        Restaurant restaurantWithUpdatedData = restaurantMapper.restaurantDTOToRestaurant(restaurantDTO);
        restaurantWithUpdatedData.setId(idOfRestaurantInDB); //Set correct ID from DB
        restaurantWithUpdatedData.setUuid(UUID.fromString(restaurantUuid)); //Set correct UUID (confirmed by ID fetching)
        //Persist and send back a new DTO
        Restaurant restaurantSaved = restaurantRepository.save(restaurantWithUpdatedData);
        return restaurantMapper.restaurantToRestaurantDTO(restaurantSaved);
    }
}
