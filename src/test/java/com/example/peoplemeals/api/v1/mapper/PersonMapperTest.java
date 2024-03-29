package com.example.peoplemeals.api.v1.mapper;

import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.domain.Person;
import org.junit.jupiter.api.Test;
import testUtils.PojoExampleCreation;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonMapperTest {

    PersonMapper personMapper = PersonMapper.INSTANCE;

    @Test
    void convertPersonToPersonDTO() {
        //given
        Person person = PojoExampleCreation.createPersonExample(1);
         //when
        PersonDTO personDTO = personMapper.personToPersonDTO(person);
        //then
        assertEquals(person.getUuid(),personDTO.getUuid());
        assertEquals(person.getFiscal(),personDTO.getFiscal());
        assertEquals(person.getFullName(),personDTO.getFullName());
        assertEquals(person.getMobile(),personDTO.getMobile());
        assertEquals(person.getTelephone(),personDTO.getTelephone());
    }

    @Test
    void convertPersonDTOToPerson() {
        //given
        PersonDTO personDTO = PojoExampleCreation.createPersonDTOExample(1);
        //when
        Person person = personMapper.personDTOToPerson(personDTO);
        //then
        assertEquals(personDTO.getUuid(),person.getUuid());
        assertEquals(personDTO.getFiscal(),person.getFiscal());
        assertEquals(personDTO.getFullName(),person.getFullName());
        assertEquals(personDTO.getMobile(),person.getMobile());
        assertEquals(personDTO.getTelephone(),person.getTelephone());
    }
}