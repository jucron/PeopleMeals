package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.PersonDTOList;
import com.example.peoplemeals.api.v1.model.PlanningDTO;
import com.example.peoplemeals.api.v1.model.forms.AssociateForm;
import com.example.peoplemeals.services.PlanningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PlanningController.BASE_URL)
@RequiredArgsConstructor
public class PlanningController {
    public static final String BASE_URL = "/api/v1/planning";
    private final PlanningService planningService;

    @PostMapping({"/associate"})
    @ResponseStatus(HttpStatus.OK)
    public PlanningDTO associatePersonToDish (@RequestBody AssociateForm associateForm) {
        return planningService.associate(associateForm);
    }
    @GetMapping({"/getPersonList/{restaurantId}/{dayOfWeek}"})
    @ResponseStatus(HttpStatus.OK)
    public PersonDTOList getPersonList (@PathVariable long restaurantId,@PathVariable String dayOfWeek) {
        return planningService.getPersonListByRestaurantAndDay(restaurantId,dayOfWeek);
    }

}
