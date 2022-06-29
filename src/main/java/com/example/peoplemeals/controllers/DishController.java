package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;
import com.example.peoplemeals.services.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dishes/")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @GetMapping({"/"})
    @ResponseStatus(HttpStatus.OK)
    public EntityDTOList<DishDTO> getAllDishes(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "name") String sortBy) {
        return dishService.getAll(pageNo, pageSize, sortBy);
    }

    @GetMapping({"/{dishUuid}"})
    @ResponseStatus(HttpStatus.OK)
    public DishDTO getDish (@PathVariable String dishUuid) {
        return dishService.get(dishUuid);
    }

    @PostMapping({"/"})
    @ResponseStatus(HttpStatus.CREATED)
    public DishDTO addDish (@RequestBody DishDTO dishDTO) {
        return dishService.add(dishDTO);
    }

    @DeleteMapping({"/{dishUuid}"})
    @ResponseStatus(HttpStatus.OK)
    public void removeDish (@PathVariable String dishUuid) {
        dishService.remove(dishUuid);
    }

    @PutMapping({"/{dishUuid}"})
    @ResponseStatus(HttpStatus.OK)
    public DishDTO updateDish (@PathVariable String dishUuid, @RequestBody DishDTO dishDTO) {
        return dishService.update(dishUuid,dishDTO);
    }
}
