package com.example.peoplemeals.api.v1.mapper;

import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.domain.Dish;
import org.junit.jupiter.api.Test;
import testUtils.PojoExampleCreation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DishMapperTest {

    DishMapper dishMapper = DishMapper.INSTANCE;

    @Test
    void convertDishToDishDTO() {
        //given
        Dish dish = PojoExampleCreation.createDishExample(1);
        //when
        DishDTO dishDTO = dishMapper.dishToDishDTO(dish);
        //then
        assertEquals(dish.getUuid(),dishDTO.getUuid());
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
        assertNotNull(dishDTO.getUuid());
        assertEquals(dish.getUuid(),dishDTO.getUuid());
        assertEquals(dishDTO.getName(),dish.getName());
        assertEquals(dishDTO.getRecipeUrl(),dish.getRecipeUrl());
    }
}