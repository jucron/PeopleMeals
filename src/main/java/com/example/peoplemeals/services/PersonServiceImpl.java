package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.PersonMapper;
import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;
import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Override
    public EntityDTOList<PersonDTO> getAll() {
        return new EntityDTOList<>(personRepository.findAll().stream()
                .map(personMapper::personToPersonDTO)
                .collect(Collectors.toSet()));
    }

    @Override
    public PersonDTO get(String personUuid) {
        return personMapper.personToPersonDTO(
                personRepository.findRequiredByUuid(personUuid));
    }

    @Override
    public PersonDTO add(PersonDTO personDTO) {
        Person personToBeSaved = personMapper.personDTOToPerson(personDTO);
        personToBeSaved.setId(null); //must remove ID to perform auto-generate
        personToBeSaved.setUuid(UUID.randomUUID());
        Person personSaved = personRepository.save(personToBeSaved);
        return personMapper.personToPersonDTO(personSaved);
    }

    @Override
    public void remove(String personUuid) {
        Person personInDB = personRepository.findRequiredByUuid(personUuid);
        personRepository.delete(personInDB);
    }

    @Override
    public PersonDTO update(String personUuid, PersonDTO personDTO) {
        Person personInDB = personRepository.findRequiredByUuid(personUuid);
        Person personWithUpdatedData = personMapper.personDTOToPerson(personDTO);
        //Set ID from original, to replace existing data when saved
        personWithUpdatedData.setId(personInDB.getId());
        Person personSaved = personRepository.save(personWithUpdatedData);
        return personMapper.personToPersonDTO(personSaved);
    }

}
