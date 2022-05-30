package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PersonController.BASE_URL)
@RequiredArgsConstructor
public class PersonController {

    public static final String BASE_URL = "/api/v1/persons";
    private final PersonService personService;

    @PostMapping({"/add"})
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDTO addPerson (@RequestBody PersonDTO personDTO) {
        return personService.add(personDTO);
    }
    @DeleteMapping({"/remove/{personId}"})
    @ResponseStatus(HttpStatus.OK)
    public void removePerson (@PathVariable Long personId) {
        personService.remove(personId);
    }
    @PutMapping({"/update/{personId}"})
    @ResponseStatus(HttpStatus.OK)
    public PersonDTO updatePerson (@PathVariable Long personId, @RequestBody PersonDTO personDTO) {
        return personService.update(personId,personDTO);
    }
}
