package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.DishMapper;
import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;
import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.repositories.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {
    private final DishRepository dishRepository;
    private final DishMapper dishMapper;

    @Override
    public EntityDTOList<DishDTO> getAll() {
        return new EntityDTOList<>(dishRepository.findAll().stream()
                .map(dishMapper::dishToDishDTO)
                .collect(Collectors.toSet()));
    }

    @Override
    public DishDTO get(String dishUuid) {
        return dishMapper.dishToDishDTO(
                dishRepository.findRequiredByUuid(dishUuid));
    }

    @Override
    public DishDTO add(DishDTO dishDTO) {
        Dish dishToBeSaved = dishMapper.dishDTOToDish(dishDTO);
        dishToBeSaved.setId(null); //must remove ID to perform auto-generate
        dishToBeSaved.setUuid(UUID.randomUUID());
        Dish dishSaved = dishRepository.save(dishToBeSaved);
        return dishMapper.dishToDishDTO(dishSaved);
    }

    @Override
    public void remove(String dishUuid) {
        dishRepository.deleteById(dishRepository.findIdRequiredByUuid(dishUuid));
    }

    @Override
    public DishDTO update(String dishUuid, DishDTO dishDTO) {
        long iDOfDishInDB = dishRepository.findIdRequiredByUuid(dishUuid);
        //Get new Data from DTO; set ID and UUID from original, to replace existing data when saved
        Dish dishWithUpdatedData = dishMapper.dishDTOToDish(dishDTO);
        dishWithUpdatedData.setId(iDOfDishInDB); //Set correct ID from DB
        dishWithUpdatedData.setUuid(UUID.fromString(dishUuid)); //Set correct UUID (confirmed by ID fetching)
        //Persist and send back a new DTO
        Dish dishSaved = dishRepository.save(dishWithUpdatedData);
        return dishMapper.dishToDishDTO(dishSaved);
    }
}
