package com.example.peoplemeals.api.v1.mapper;

import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.helpers.PojoExampleCreation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonMapperTest {

    PersonMapper personMapper = PersonMapper.INSTANCE;

    @Test
    void convertPersonToPersonDTO() {
        //given
        Person person = PojoExampleCreation.createPersonExample();
         //when
        PersonDTO personDTO = personMapper.personToPersonDTO(person);
        //then
        assertEquals(person.getId(),personDTO.getId());
        assertEquals(person.getFiscal(),personDTO.getFiscal());
        assertEquals(person.getFullName(),personDTO.getFullName());
        assertEquals(person.getMobile(),personDTO.getMobile());
        assertEquals(person.getTelephone(),personDTO.getTelephone());
        assertEquals(person.getUsername(),personDTO.getUsername());
    }
}