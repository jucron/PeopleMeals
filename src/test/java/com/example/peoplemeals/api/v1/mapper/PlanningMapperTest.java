package com.example.peoplemeals.api.v1.mapper;

import com.example.peoplemeals.api.v1.model.PlanningDTO;
import com.example.peoplemeals.domain.Planning;
import com.example.peoplemeals.helpers.PojoExampleCreation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlanningMapperTest {

    PlanningMapper planningMapper = PlanningMapper.INSTANCE;

    @Test
    void convertPlanningToPlanningDTO() {
        //given
        Planning planning = PojoExampleCreation.createPlanningExample(1);
        //when
        PlanningDTO planningDTO = planningMapper.planningToPlanningDTO(planning);
        //then
        assertEquals(planning.getId(),planningDTO.getId());
        assertEquals(planning.getDayOfWeek(),planningDTO.getDayOfWeek());
        assertEquals(planning.getId(),planningDTO.getId());

        assertEquals(planning.getDish().getUuid(),planningDTO.getDishDTO().getUuid());
        assertEquals(planning.getDish().getName(),planningDTO.getDishDTO().getName());
        assertEquals(planning.getDish().getRecipeUrl(),planningDTO.getDishDTO().getRecipeUrl());

        assertEquals(planning.getRestaurant().getId(),planningDTO.getRestaurantDTO().getId());
        assertEquals(planning.getRestaurant().getName(),planningDTO.getRestaurantDTO().getName());
        assertEquals(planning.getRestaurant().getStaffRestDay(),planningDTO.getRestaurantDTO().getStaffRestDay());
        assertEquals(planning.getRestaurant().getOpeningTime(),planningDTO.getRestaurantDTO().getOpeningTime());
        assertEquals(planning.getRestaurant().getClosingTime(),planningDTO.getRestaurantDTO().getClosingTime());
    }
}