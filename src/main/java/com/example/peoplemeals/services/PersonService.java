package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;

public interface PersonService {

    PersonDTO get(String personUuid);

    EntityDTOList<PersonDTO> getAll();

    PersonDTO add(PersonDTO personDTO);

    void remove(String personUuid);

    PersonDTO update(String personUuid, PersonDTO personDTO);
}
