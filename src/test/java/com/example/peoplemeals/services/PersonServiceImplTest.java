package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.PersonMapper;
import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.helpers.PojoExampleCreation;
import com.example.peoplemeals.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    private PersonService personService;

    @Mock
    private PersonRepository personRepository;
    @Mock
    private PersonMapper personMapper;
    @Captor
    private ArgumentCaptor<Person> personArgumentCaptor;

    @BeforeEach
    public void setUp(){
        personService = new PersonServiceImpl(personRepository,personMapper);
    }
    @Test
    void add() {
        //given
        PersonDTO personDTO = PojoExampleCreation.createPersonDTOExample(1);
        when(personMapper.personDTOToPerson(personDTO)).thenReturn(new Person().withId(50L));
        //when
        PersonDTO personSavedDTO = personService.add(personDTO);
        //then
        verify(personRepository).save(personArgumentCaptor.capture());  //Person is persisted
        verify(personMapper).personDTOToPerson(any(PersonDTO.class));   //PersonDTO is mapped to be saved
        verify(personMapper).personToPersonDTO(any(Person.class));      //Person is mapped back to DTO and returned

        Person personCaptured = personArgumentCaptor.getValue();    //Capture the object saved
        assertNull(personCaptured.getId());         //Assert that ID is null before persisted
    }
    @Test
    void remove() {
        Long personId = 1L;
        //when
        personService.remove(personId);
        //then
        verify(personRepository).deleteById(personId);
    }
    @Test
    void update() {
        //given
        Long personId = 10L;
        PersonDTO personDTO = PojoExampleCreation.createPersonDTOExample(1);
        when(personRepository.findById(personId)).thenReturn(Optional.of(new Person().withId(personId)));
        when(personMapper.personDTOToPerson(personDTO)).thenReturn(new Person().withId(personDTO.getId()));
        //when
        PersonDTO personDTOUpdated = personService.update(personId,personDTO);
        //then
        verify(personRepository).findById(personId);       //Person is fetched by ID
        verify(personRepository).save(personArgumentCaptor.capture());  //Person is updated
        verify(personMapper).personDTOToPerson(any(PersonDTO.class));   //PersonDTO is mapped to be Updated
        verify(personMapper).personToPersonDTO(any(Person.class));      //Person is mapped back to DTO and returned

        Person personCaptured = personArgumentCaptor.getValue();    //Capture the object saved
        assertEquals(personId,personCaptured.getId());              //Assert that ID was the same as fetched before persisted

    }
}