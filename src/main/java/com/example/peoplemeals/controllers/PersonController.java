package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;
import com.example.peoplemeals.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persons/")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping({"/"})
    @ResponseStatus(HttpStatus.OK)
    public EntityDTOList<PersonDTO> getAllPersons(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "fullName") String sortBy) {
        return personService.getAll(pageNo, pageSize, sortBy);
    }

    @GetMapping({"/{personUuid}"})
    @ResponseStatus(HttpStatus.OK)
    public PersonDTO getPerson (@PathVariable String personUuid) {
        return personService.get(personUuid);
    }

    @PostMapping({"/"})
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDTO addPerson(@RequestBody PersonDTO personDTO) {
        return personService.add(personDTO);
    }

    @DeleteMapping({"/{personUuid}"})
    @ResponseStatus(HttpStatus.OK)
    public void removePerson(@PathVariable String personUuid) {
        personService.remove(personUuid);
    }

    @PutMapping({"/{personUuid}"})
    @ResponseStatus(HttpStatus.OK)
    public PersonDTO updatePerson(@PathVariable String personUuid, @RequestBody PersonDTO personDTO) {
        return personService.update(personUuid, personDTO);
    }
}
