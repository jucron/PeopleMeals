package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.RestaurantDTO;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;
import com.example.peoplemeals.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants/")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping({"/"})
    @ResponseStatus(HttpStatus.OK)
    public EntityDTOList<RestaurantDTO> getAllRestaurants(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "name") String sortBy) {
        return restaurantService.getAll(pageNo, pageSize, sortBy);
    }

    @GetMapping({"/{restaurantUuid}"})
    @ResponseStatus(HttpStatus.OK)
    public RestaurantDTO getRestaurant (@PathVariable String restaurantUuid) {
        return restaurantService.get(restaurantUuid);
    }


    @PostMapping({"/"})
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantDTO addRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        return restaurantService.add(restaurantDTO);
    }

    @DeleteMapping({"/{restaurantUuid}"})
    @ResponseStatus(HttpStatus.OK)
    public void removeRestaurant(@PathVariable String restaurantUuid) {
        restaurantService.remove(restaurantUuid);
    }

    @PutMapping({"/{restaurantUuid}"})
    @ResponseStatus(HttpStatus.OK)
    public RestaurantDTO updateRestaurant(@PathVariable String restaurantUuid, @RequestBody RestaurantDTO restaurantDTO) {
        return restaurantService.update(restaurantUuid, restaurantDTO);
    }

}
