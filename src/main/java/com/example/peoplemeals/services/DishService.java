package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;

public interface DishService {
    DishDTO add(DishDTO dishDTO);

    void remove(String dishUuid);

    DishDTO update(String dishUuid, DishDTO dishDTO);

    EntityDTOList<DishDTO> getAll();

    DishDTO get(String dishUuid);
}
