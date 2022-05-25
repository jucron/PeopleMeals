package com.example.peoplemeals.api.v1.mapper;

import com.example.peoplemeals.api.v1.model.RestaurantDTO;
import com.example.peoplemeals.domain.Restaurant;
import com.example.peoplemeals.helpers.PojoExampleCreation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestaurantMapperTest {

    RestaurantMapper restaurantMapper = RestaurantMapper.INSTANCE;

    @Test
    void convertRestaurantToRestaurantDTO() {
        //given
        Restaurant restaurant = PojoExampleCreation.createRestaurantExample();
        //when
        RestaurantDTO restaurantDTO = restaurantMapper.restaurantToRestaurantDTO(restaurant);
        //then
        assertEquals(restaurant.getId(),restaurantDTO.getId());
        assertEquals(restaurant.getName(),restaurantDTO.getName());
        assertEquals(restaurant.getOpeningTime(),restaurantDTO.getOpeningTime());
        assertEquals(restaurant.getClosingTime(),restaurantDTO.getClosingTime());
        assertEquals(restaurant.getStaffRestDay(),restaurantDTO.getStaffRestDay());

        assertEquals(restaurant.getDishes().size(),restaurantDTO.getDishDTOS().size());
    }
}