package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.DishMapper;
import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;
import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.repositories.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {
    private final DishRepository dishRepository;
    private final DishMapper dishMapper;

    @Override
    public EntityDTOList<DishDTO> getAll() {
        return null;
    }

    @Override
    public DishDTO get(String dishUuid) {
        return null;
    }

    @Override
    public DishDTO add(DishDTO dishDTO) {
        Dish dishToBeSaved = dishMapper.dishDTOToDish(dishDTO);
        dishToBeSaved.setId(null); //must remove ID to perform auto-generate
        dishToBeSaved.setUuid(UUID.randomUUID());
        Dish savedEntity = dishRepository.save(dishToBeSaved);
        return dishMapper.dishToDishDTO(savedEntity);
    }

    @Override
    public void remove(String dishUuid) {
        Dish dishInDB = dishRepository.findRequiredByUuid(dishUuid);
        dishRepository.delete(dishInDB);
    }

    @Override
    public DishDTO update(String dishUuid, DishDTO dishDTO) {
        Dish dishInDB = dishRepository.findRequiredByUuid(dishUuid);
        Dish dishWithUpdatedData = dishMapper.dishDTOToDish(dishDTO);
        //Set ID from original, to replace existing data when saved
        dishWithUpdatedData.setId(dishInDB.getId());
        Dish dishSaved = dishRepository.save(dishWithUpdatedData);
        return dishMapper.dishToDishDTO(dishSaved);
    }


}
