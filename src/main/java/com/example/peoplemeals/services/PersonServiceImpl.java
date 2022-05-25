package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.model.PersonDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    @Override
    public PersonDTO add(PersonDTO personDTO) {
        return null;
    }

    @Override
    public PersonDTO remove(Long personId) {
        return null;
    }

    @Override
    public PersonDTO update(Long id, PersonDTO personDTO) {
        return null;
    }
}
