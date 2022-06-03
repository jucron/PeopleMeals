package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.PersonMapper;
import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Override
    public PersonDTO add(PersonDTO personDTO) {
            Person personToBeSaved = personMapper.personDTOToPerson(personDTO);
            personToBeSaved.setId(null); //must remove ID to perform auto-generate
            personRepository.save(personToBeSaved);
            return personMapper.personToPersonDTO(personToBeSaved);
    }
    @Override
    public void remove(Long personId) {
        personRepository.deleteById(personId);
    }

    @Override
    public PersonDTO update(Long id, PersonDTO personDTO) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (personOptional.isEmpty()) {
            throw new IllegalArgumentException();
        }
            Person personWithUpdatedData = personMapper.personDTOToPerson(personDTO);
            personWithUpdatedData.setId(personOptional.get().getId());
            personRepository.save(personWithUpdatedData);
            return personMapper.personToPersonDTO(personWithUpdatedData);
    }
}
