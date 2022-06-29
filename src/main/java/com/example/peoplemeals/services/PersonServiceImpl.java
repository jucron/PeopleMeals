package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.PersonMapper;
import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;
import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Override
    public EntityDTOList<PersonDTO> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        return new EntityDTOList<>(personRepository
                .findAll(PageRequest.of(pageNo, pageSize, Sort.by(sortBy))).getContent()
                .stream()
                .map(personMapper::personToPersonDTO)
                .collect(Collectors.toList()));
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
        personRepository.deleteById(personRepository.findIdRequiredByUuid(personUuid));
    }

    @Override
    public PersonDTO update(String personUuid, PersonDTO personDTO) {
        long iDOfPersonInDB = personRepository.findIdRequiredByUuid(personUuid);
        //Get new Data from DTO; set ID and UUID from original, to replace existing data when saved
        Person personWithUpdatedData = personMapper.personDTOToPerson(personDTO);
        personWithUpdatedData.setId(iDOfPersonInDB); //Set correct ID from DB
        personWithUpdatedData.setUuid(UUID.fromString(personUuid)); //Set correct UUID (confirmed by ID fetching)
        //Persist and send back a new DTO
        Person personSaved = personRepository.save(personWithUpdatedData);
        return personMapper.personToPersonDTO(personSaved);
    }

}
