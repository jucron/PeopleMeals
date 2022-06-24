package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.forms.UserForm;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;
import com.example.peoplemeals.domain.security.Credentials;
import com.example.peoplemeals.services.CredentialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credentials/")
@RequiredArgsConstructor
public class CredentialsController {
    private final CredentialsService credentialsService;

    @GetMapping({"/"})
    @ResponseStatus(HttpStatus.OK)
    public EntityDTOList<Credentials> getUsers() {
        return credentialsService.getAll();
    }

    @PostMapping({"/"})
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserForm form) {
        credentialsService.createUser(form);
    }

    @DeleteMapping({"/{username}"})
    @ResponseStatus(HttpStatus.OK)
    public void deactivateUser(@PathVariable String username) {
        credentialsService.deactivateUser(username);
    }
}
