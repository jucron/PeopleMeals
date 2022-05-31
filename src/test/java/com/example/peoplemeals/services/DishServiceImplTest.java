package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.DishMapper;
import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.helpers.PojoExampleCreation;
import com.example.peoplemeals.repositories.DishRepository;
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
class DishServiceImplTest {

    private DishService dishService;
    @Mock
    private DishRepository dishRepository;
    @Mock
    private DishMapper dishMapper;
    @Captor
    private ArgumentCaptor<Dish> dishArgumentCaptor;


    @BeforeEach
    public void setUp(){
        dishService = new DishServiceImpl(dishRepository,dishMapper);
    }
    @Test
    void add() {
        //given
        DishDTO dishDTO = PojoExampleCreation.createDishDTOExample(1);
        when(dishMapper.dishDTOToDish(dishDTO)).thenReturn(new Dish().withId(50L));
        //when
        DishDTO dishSavedDTO = dishService.add(dishDTO);
        //then
        verify(dishRepository).save(dishArgumentCaptor.capture());  //Dish is persisted
        verify(dishMapper).dishDTOToDish(any(DishDTO.class));   //DishDTO is mapped to be saved
        verify(dishMapper).dishToDishDTO(any(Dish.class));      //Dish is mapped back to DTO and returned

        Dish dishCaptured = dishArgumentCaptor.getValue();    //Capture the object saved
        assertNull(dishCaptured.getId());         //Assert that ID is null before persisted
    }
    @Test
    void remove() {
        Long dishId = 1L;
        //when
        dishService.remove(dishId);
        //then
        verify(dishRepository).deleteById(dishId);
    }
    @Test
    void update() {
        //given
        Long dishId = 10L;
        DishDTO dishDTO = PojoExampleCreation.createDishDTOExample(1);
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(new Dish().withId(dishId)));
        when(dishMapper.dishDTOToDish(dishDTO)).thenReturn(new Dish().withId(dishDTO.getId()));
        //when
        DishDTO dishSavedDTO = dishService.update(dishId,dishDTO);
        //then
        verify(dishRepository).findById(dishId);       //Dish is fetched by ID
        verify(dishRepository).save(dishArgumentCaptor.capture());  //Dish is updated
        verify(dishMapper).dishDTOToDish(any(DishDTO.class));   //DishDTO is mapped to be updated
        verify(dishMapper).dishToDishDTO(any(Dish.class));      //Dish is mapped back to DTO and returned

        Dish dishCaptured = dishArgumentCaptor.getValue();    //Capture the object saved
        assertEquals(dishId,dishCaptured.getId());              //Assert that ID was the same as fetched before persisted
    }


}