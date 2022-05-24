package com.example.peoplemeals.api.v1.mapper;

import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.domain.Dish;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DishMapperTest {

    DishMapper dishMapper = DishMapper.INSTANCE;

    @Test
    void dishToDishDTO() {
        //given
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("dishName");
        dish.setRecipeUrl("dishUrl");
        //when
        DishDTO dishDTO = dishMapper.dishToDishDTO(dish);
        //then
        assertEquals(dish.getId(),dishDTO.getId());
        assertEquals(dish.getName(),dishDTO.getName());
        assertEquals(dish.getRecipeUrl(),dishDTO.getRecipeUrl());
    }
}