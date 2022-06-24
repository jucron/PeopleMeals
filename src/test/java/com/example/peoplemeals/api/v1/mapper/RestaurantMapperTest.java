package com.example.peoplemeals.api.v1.mapper;

import com.example.peoplemeals.api.v1.model.RestaurantDTO;
import com.example.peoplemeals.domain.Restaurant;
import org.junit.jupiter.api.Test;
import testUtils.PojoExampleCreation;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestaurantMapperTest {

    RestaurantMapper restaurantMapper = RestaurantMapper.INSTANCE;

    @Test
    void convertRestaurantToRestaurantDTO() {
        //given
        Restaurant restaurant = PojoExampleCreation.createRestaurantExample(1);
        //when
        RestaurantDTO restaurantDTO = restaurantMapper.restaurantToRestaurantDTO(restaurant);
        //then
        assertEquals(restaurant.getUuid(),restaurantDTO.getUuid());
        assertEquals(restaurant.getName(),restaurantDTO.getName());
        assertEquals(restaurant.getOpeningTime(),restaurantDTO.getOpeningTime());
        assertEquals(restaurant.getClosingTime(),restaurantDTO.getClosingTime());
        assertEquals(restaurant.getStaffRestDay(),restaurantDTO.getStaffRestDay());

        assertEquals(restaurant.getDishes().size(),restaurantDTO.getDishDTOS().size());
    }
    @Test
    void convertRestaurantDTOToRestaurant() {
        //given
        RestaurantDTO restaurantDTO = PojoExampleCreation.createRestaurantDTOExample(1);
        //when
        Restaurant restaurant = restaurantMapper.restaurantDTOToRestaurant(restaurantDTO);
        //then
        assertEquals(restaurantDTO.getUuid(),restaurant.getUuid());
        assertEquals(restaurantDTO.getName(),restaurant.getName());
        assertEquals(restaurantDTO.getOpeningTime(),restaurant.getOpeningTime());
        assertEquals(restaurantDTO.getClosingTime(),restaurant.getClosingTime());
        assertEquals(restaurantDTO.getStaffRestDay(),restaurant.getStaffRestDay());

        assertEquals(restaurantDTO.getDishDTOS().size(),restaurant.getDishes().size());
    }
}