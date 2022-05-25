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

        assertEquals(planning.getDish().getId(),planningDTO.getDishDTO().getId());
        assertEquals(planning.getDish().getName(),planningDTO.getDishDTO().getName());
        assertEquals(planning.getDish().getRecipeUrl(),planningDTO.getDishDTO().getRecipeUrl());
    }
}