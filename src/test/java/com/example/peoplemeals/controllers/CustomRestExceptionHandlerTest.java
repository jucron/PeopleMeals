package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.mapper.DishMapper;
import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.repositories.DishRepository;
import com.example.peoplemeals.services.DishServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CustomRestExceptionHandlerTest {

    @InjectMocks
    private DishServiceImpl dishService;
    @Mock
    DishRepository dishRepository;
    @Mock
    DishMapper dishMapper;

    //todo: create a integration test for this
    @Test
    void handleIllegalArgumentException() {
        //when
//        when(dishRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        assertThrows(IllegalArgumentException.class,() -> dishService.add(null));
        assertThrows(NoSuchElementException.class,() -> dishService.remove(1L));
        assertThrows(NoSuchElementException.class,() -> dishService.update(1L, new DishDTO()));

    }
    @Test
    void handleNullPointerException() {


    }

    @Test
    void handleNoSuchElementException() {
//        when(dishRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,() -> dishService.remove(1L));
        assertThrows(NoSuchElementException.class,() -> dishService.update(1L, new DishDTO()));
    }

    @Test
    void handleAnyException() {
    }
}