package com.example.peoplemeals.api.v1.mapper;

import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.helpers.PojoExampleCreation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DishMapperTest {

    DishMapper dishMapper = DishMapper.INSTANCE;

    @Test
    void convertDishToDishDTO() {
        //given
        Dish dish = PojoExampleCreation.createDishExample(1);
        //when
        DishDTO dishDTO = dishMapper.dishToDishDTO(dish);
        //then
        assertEquals(dish.getId(),dishDTO.getId());
        assertEquals(dish.getName(),dishDTO.getName());
        assertEquals(dish.getRecipeUrl(),dishDTO.getRecipeUrl());
    }
    @Test
    void convertDishDTOToDish() {
        //given
        DishDTO dishDTO = PojoExampleCreation.createDishDTOExample(1);
        //when
        Dish dish = dishMapper.dishDTOToDish(dishDTO);
        //then
        assertEquals(dishDTO.getId(),dish.getId());
        assertEquals(dishDTO.getName(),dish.getName());
        assertEquals(dishDTO.getRecipeUrl(),dish.getRecipeUrl());
    }
}