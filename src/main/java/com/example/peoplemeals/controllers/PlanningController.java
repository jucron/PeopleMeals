package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.api.v1.model.PlanningDTO;
import com.example.peoplemeals.api.v1.model.forms.AssociateForm;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;
import com.example.peoplemeals.services.PlanningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PlanningController.BASE_URL)
@RequiredArgsConstructor
public class PlanningController {
    public static final String BASE_URL = "/plannings/";
    private final PlanningService planningService;

    @GetMapping({"/"})
    @ResponseStatus(HttpStatus.OK)
    public EntityDTOList<PlanningDTO> getAllPlannings () {
        return planningService.getAll();
    }

    @GetMapping({"/{planningUuid}"})
    @ResponseStatus(HttpStatus.OK)
    public PlanningDTO getPlanning (@PathVariable String planningUuid) {
        return planningService.get(planningUuid);
    }

    @PostMapping({"/"})
    @ResponseStatus(HttpStatus.OK)
    public PlanningDTO associatePerson(@RequestBody AssociateForm associateForm) {
        return planningService.associate(associateForm);
    }

    @DeleteMapping({"/"})
    @ResponseStatus(HttpStatus.OK)
    public void disassociatePerson(@RequestBody AssociateForm associateForm) {
        planningService.disassociate(associateForm);
    }

    @DeleteMapping({"/{planningUuid}"})
    @ResponseStatus(HttpStatus.OK)
    public void disassociatePerson(@PathVariable String planningUuid) {
        planningService.remove(planningUuid);
    }

    @GetMapping({"/restaurant/{restaurantUuid}/{dayOfWeek}"})
    @ResponseStatus(HttpStatus.OK)
    public EntityDTOList<PersonDTO> getPersonListByRestaurantAndDay(@PathVariable String restaurantUuid, @PathVariable String dayOfWeek) {
        return planningService.getPersonListByRestaurantAndDay(restaurantUuid, dayOfWeek);
    }

    @GetMapping({"/dish/{dishUuid}/{dayOfWeek}"})
    @ResponseStatus(HttpStatus.OK)
    public EntityDTOList<PersonDTO> getPersonListByDishAndDay(@PathVariable String dishUuid, @PathVariable String dayOfWeek) {
        return planningService.getPersonListByDishAndDay(dishUuid, dayOfWeek);
    }

    @GetMapping({"/no_dish/{dayOfWeek}"})
    @ResponseStatus(HttpStatus.OK)
    public EntityDTOList<PersonDTO> getPersonListWithNoDishByDay(@PathVariable String dayOfWeek) {
        return planningService.getPersonListWithNoDishByDay(dayOfWeek);
    }

}
