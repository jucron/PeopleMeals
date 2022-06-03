package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.DishMapper;
import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.repositories.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
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
        Dish dishInDB = checkElementPresence(id);
        dishRepository.delete(dishInDB);
    }
    @Override
    public DishDTO update(Long id, DishDTO dishDTO) {
        Dish dishInDB = checkElementPresence(id);
        Dish dishWithUpdatedData = dishMapper.dishDTOToDish(dishDTO);
        dishWithUpdatedData.setId(dishInDB.getId());
        dishRepository.save(dishWithUpdatedData);
        return dishMapper.dishToDishDTO(dishWithUpdatedData);
    }
    private Dish checkElementPresence(Long id) {
        Optional<Dish> dishOptional = dishRepository.findById(id);
        if (dishOptional.isEmpty()) {
            throw new NoSuchElementException("No Dish with this ID was found in Database");
        }
        return dishOptional.get();
    }
}
