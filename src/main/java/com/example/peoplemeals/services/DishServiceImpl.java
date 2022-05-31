package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.model.DishDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {
    @Override
    public DishDTO add(DishDTO dishDTO) {
        return null;
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public DishDTO update(Long id, DishDTO dishDTO) {
        return null;
    }
}
