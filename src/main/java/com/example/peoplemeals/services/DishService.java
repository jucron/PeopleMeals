package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.model.DishDTO;

public interface DishService {
    DishDTO add(DishDTO dishDTO);

    void remove(String dishUuid);

    DishDTO update(String dishUuid, DishDTO dishDTO);
}
