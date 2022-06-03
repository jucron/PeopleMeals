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
        Person personInDB = checkElementPresence(personId);
        personRepository.delete(personInDB);
    }
    @Override
    public PersonDTO update(Long personId, PersonDTO personDTO) {
        Person personInDB = checkElementPresence(personId);
        Person personWithUpdatedData = personMapper.personDTOToPerson(personDTO);
        personWithUpdatedData.setId(personInDB.getId());
        personRepository.save(personWithUpdatedData);
        return personMapper.personToPersonDTO(personWithUpdatedData);
    }
    private Person checkElementPresence(Long personId) {
        Optional<Person> personOptional = personRepository.findById(personId);
        if (personOptional.isEmpty()) {
            throw new IllegalArgumentException("No Person with this ID was found in Database");
        }
        return personOptional.get();
    }
}
