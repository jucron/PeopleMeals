package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.RestaurantMapper;
import com.example.peoplemeals.api.v1.model.RestaurantDTO;
import com.example.peoplemeals.domain.Restaurant;
import com.example.peoplemeals.helpers.PojoExampleCreation;
import com.example.peoplemeals.repositories.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {
    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private RestaurantMapper restaurantMapper;
    @Captor
    private ArgumentCaptor<Restaurant> restaurantArgumentCaptor;

    @BeforeEach
    public void setUp(){
        restaurantService = new RestaurantServiceImpl(restaurantRepository,restaurantMapper);
    }

    @Test
    void add() {
        //given
        RestaurantDTO restaurantDTO = PojoExampleCreation.createRestaurantDTOExample(1);
        when(restaurantMapper.restaurantDTOToRestaurant(restaurantDTO)).thenReturn(new Restaurant().withId(50L));
        //when
        RestaurantDTO restaurantSavedDTO = restaurantService.add(restaurantDTO);
        //then
        verify(restaurantRepository).save(restaurantArgumentCaptor.capture());  //Restaurant is persisted
        verify(restaurantMapper).restaurantDTOToRestaurant(any(RestaurantDTO.class));   //RestaurantDTO is mapped to be saved
        verify(restaurantMapper).restaurantToRestaurantDTO(any(Restaurant.class));      //Restaurant is mapped back to DTO and returned

        Restaurant restaurantCaptured = restaurantArgumentCaptor.getValue();    //Capture the object saved
        assertNull(restaurantCaptured.getId());         //Assert that ID is null before persisted
    }

    @Test
    void remove() {
        Long restaurantId = 1L;
        //when
        restaurantService.remove(restaurantId);
        //then
        verify(restaurantRepository).deleteById(restaurantId);
    }

    @Test
    void update() {
        //given
        Long restaurantId = 10L;
        RestaurantDTO restaurantDTO = PojoExampleCreation.createRestaurantDTOExample(1);
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(new Restaurant().withId(restaurantId)));
        when(restaurantMapper.restaurantDTOToRestaurant(restaurantDTO)).thenReturn(new Restaurant().withId(restaurantDTO.getId()));
        //when
        RestaurantDTO restaurantDTOUpdated = restaurantService.update(restaurantId,restaurantDTO);
        //then
        verify(restaurantRepository).findById(restaurantId);       //Restaurant is fetched by ID
        verify(restaurantRepository).save(restaurantArgumentCaptor.capture());  //Restaurant is persisted
        verify(restaurantMapper).restaurantDTOToRestaurant(any(RestaurantDTO.class));   //RestaurantDTO is mapped to be saved
        verify(restaurantMapper).restaurantToRestaurantDTO(any(Restaurant.class));      //Restaurant is mapped back to DTO and returned

        Restaurant restaurantCaptured = restaurantArgumentCaptor.getValue();    //Capture the object saved
        assertEquals(restaurantId,restaurantCaptured.getId());              //Assert that ID was the same as fetched before persisted

    }


}