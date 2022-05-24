package com.example.peoplemeals.api.v1.mapper;

import com.example.peoplemeals.api.v1.model.PlanningDTO;
import com.example.peoplemeals.domain.Planning;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlanningMapper {

    PlanningMapper INSTANCE = Mappers.getMapper(PlanningMapper.class);

    @Mapping(source = "dish", target = "dishDTO")
    @Mapping(source = "person", target = "personDTO")
    PlanningDTO planningToPlanningDTO (Planning planning);

}
