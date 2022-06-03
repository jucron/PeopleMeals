package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.DishMapper;
import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.repositories.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {
    private final DishRepository dishRepository;
    private final DishMapper dishMapper;

    @Override
    public DishDTO add(DishDTO dishDTO) {
        Dish dishToBeSaved = dishMapper.dishDTOToDish(dishDTO);
        dishToBeSaved.setId(null); //must remove ID to perform auto-generate
        dishRepository.save(dishToBeSaved);
        return dishMapper.dishToDishDTO(dishToBeSaved);
    }
    @Override
    public void remove(Long id) {
        dishRepository.deleteById(id);
    }
    @Override
    public DishDTO update(Long id, DishDTO dishDTO) {
        Optional<Dish> dishOptional = dishRepository.findById(id);
        if (dishOptional.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Dish dishWithUpdatedData = dishMapper.dishDTOToDish(dishDTO);
        dishWithUpdatedData.setId(dishOptional.get().getId());
        dishRepository.save(dishWithUpdatedData);
        return dishMapper.dishToDishDTO(dishWithUpdatedData);
    }
}
