package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.model.PersonDTO;

public interface PersonService {
    PersonDTO add(PersonDTO personDTO);

    PersonDTO remove(Long personId);

    PersonDTO update(Long id, PersonDTO personDTO);
}
