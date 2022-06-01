package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.RestaurantDTO;
import com.example.peoplemeals.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RestaurantController.BASE_URL)
@RequiredArgsConstructor
public class RestaurantController {

    public static final String BASE_URL = "/api/v1/restaurants";
    private final RestaurantService restaurantService;

    @PostMapping({"/add"})
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantDTO addRestaurant (@RequestBody RestaurantDTO restaurantDTO) {
        return restaurantService.add(restaurantDTO);
    }
    @DeleteMapping({"/remove/{restaurantId}"})
    @ResponseStatus(HttpStatus.OK)
    public void removeRestaurant (@PathVariable Long restaurantId) {
        restaurantService.remove(restaurantId);
    }
    @PutMapping({"/update/{restaurantId}"})
    @ResponseStatus(HttpStatus.OK)
    public RestaurantDTO updateRestaurant (@PathVariable Long restaurantId, @RequestBody RestaurantDTO restaurantDTO) {
        return restaurantService.update(restaurantId,restaurantDTO);
    }

}
