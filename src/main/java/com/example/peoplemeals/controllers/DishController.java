package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.services.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DishController.BASE_URL)
@RequiredArgsConstructor
public class DishController {

    public static final String BASE_URL = "/api/v1/dishes";
    private final DishService dishService;

    @PostMapping({"/add"})
    @ResponseStatus(HttpStatus.CREATED)
    public DishDTO addDish (@RequestBody DishDTO dishDTO) {
        return dishService.add(dishDTO);
    }
    @DeleteMapping({"/remove/{dishId}"})
    @ResponseStatus(HttpStatus.OK)
    public void removeDish (@PathVariable Long dishId) {
        dishService.remove(dishId);
    }
    @PutMapping({"/update/{dishId}"})
    @ResponseStatus(HttpStatus.OK)
    public DishDTO updateDish (@PathVariable Long dishId, @RequestBody DishDTO dishDTO) {
        return dishService.update(dishId,dishDTO);
    }
}
