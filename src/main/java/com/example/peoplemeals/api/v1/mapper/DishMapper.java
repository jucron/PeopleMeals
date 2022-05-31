package com.example.peoplemeals.api.v1.mapper;

import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.domain.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DishMapper {

    DishMapper INSTANCE = Mappers.getMapper(DishMapper.class);

    DishDTO dishToDishDTO(Dish dish);

    Dish dishDTOToDish(DishDTO dishDTO);
}
