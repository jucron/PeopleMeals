package com.example.peoplemeals.api.v1.mapper;

import com.example.peoplemeals.api.v1.model.RestaurantDTO;
import com.example.peoplemeals.domain.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestaurantMapper {

    RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);

    @Mapping(source = "dishes", target = "dishDTOS")
    RestaurantDTO restaurantToRestaurantDTO(Restaurant restaurant);
    @Mapping(source = "dishDTOS", target = "dishes")
    Restaurant restaurantDTOToRestaurant(RestaurantDTO restaurantDTO);

}
