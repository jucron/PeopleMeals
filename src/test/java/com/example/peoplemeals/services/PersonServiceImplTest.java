package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.PersonMapper;
import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.helpers.PojoExampleCreation;
import com.example.peoplemeals.repositories.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
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
    public void setUpForAll(){
        //Instantiate service class
        personService = new PersonServiceImpl(personRepository,personMapper);
    }

    @Nested
    class SuccessfulServices {

        @Test
        void getAllElements() {
            when(personRepository.findAll()).thenReturn(List.of(new Person()));
            //when
            personService.getAll();
            //then
            verify(personRepository).findAll();
            verify(personMapper,times(1)).personToPersonDTO(any(Person.class));
        }

        @Nested
        class GetAndRemoveMethods {
            private final String person_UUID = "personUuid_example";

            @BeforeEach
            public void setUpCommonDataAndStubs(){
                when(personRepository.findRequiredByUuid(anyString())).thenReturn((new Person()));
            }

            @Test
            void getASingleElement() {
                //when
                personService.get(person_UUID);
                //then
                verify(personMapper,times(1)).personToPersonDTO(any(Person.class));
            }

            @Test
            void removeAnExistingObject() {
                //when
                personService.remove(person_UUID);
                verify(personRepository).delete(any(Person.class));
            }

            @AfterEach
            void checkCommonAsserts() {
                //then
                verify(personRepository).findRequiredByUuid(person_UUID);
            }
        }

        @Nested
        class AddAndUpdateMethods {

            @BeforeEach
            public void setUpCommonDataAndStubs(){
                //given data
                PersonDTO personDTO = PojoExampleCreation.createPersonDTOExample(1);
                Person person = PojoExampleCreation.createPersonExample(2);
                person.setUuid(null); //setting UUID null, to check if 'add' method assign a value
                //given stubbing
                when(personMapper.personDTOToPerson(any(PersonDTO.class))).thenReturn(person);
                when(personMapper.personToPersonDTO(any(Person.class))).thenReturn(personDTO);
                when(personRepository.save(any(Person.class))).thenReturn(person);
            }

            @Test
            void addANewObjectToDatabase() {
                //when
                PersonDTO personSavedDTO = personService.add(new PersonDTO());
                //then
                verify(personRepository).save(personArgumentCaptor.capture());  //Entity is persisted

                Person personCaptured = personArgumentCaptor.getValue();    //Capture the object saved
                assertNull(personCaptured.getId());         //Assert that an ID is null before persisted
                assertNotNull(personCaptured.getUuid());    //Assert that a UUID is assigned before persisted
            }

            @Test
            void updateAnExistingObjectFromDatabase() {
                //given
                String personUuid = "personUuid_example";
                long personIdFromDB = 15L;
                when(personRepository.findRequiredByUuid(personUuid)).thenReturn((
                        new Person().withId(personIdFromDB)));
                //when
                PersonDTO personUpdatedDTO = personService.update(personUuid,new PersonDTO());
                //then
                verify(personRepository).findRequiredByUuid(personUuid);       //Entity is fetched by ID
                verify(personRepository).save(personArgumentCaptor.capture());  //Entity is updated

                Person personCaptured = personArgumentCaptor.getValue();      //Capture the object saved
                assertEquals(personIdFromDB,personCaptured.getId());   //Assert that ID was the same as fetched before persisted
            }

            @AfterEach
            void checkCommonAsserts() {
                //then
                verify(personMapper).personDTOToPerson(any(PersonDTO.class));   //Entity DTO is mapped to original
                verify(personMapper).personToPersonDTO(any(Person.class));      //Entity is mapped back to DTO and returned
            }
        }
    }

    @Nested
    class FailedServices {
        @Test
        void anyService_NullObjects() {
            //given expected behavior
            when(personRepository.findRequiredByUuid(null)).thenThrow(IllegalArgumentException.class);
            //when-then
            assertThrows(NullPointerException.class,()-> personService.add(null));
            assertThrows(IllegalArgumentException.class,()-> personService.remove(null));
            assertThrows(IllegalArgumentException.class,()-> personService.update(null, new PersonDTO()));
            assertThrows(NullPointerException.class,()-> personService.update("some-uuid", null));
            assertThrows(IllegalArgumentException.class,()-> personService.get(null));
        }

        @Nested
        class AccessingNonExistingObjectsInDatabase {
            private final String nonExistingUuid = "uuid-example";

            @BeforeEach
            void stubbingExpectedBehaviours() {
                when(personRepository.findRequiredByUuid(nonExistingUuid)).thenThrow(NoSuchElementException.class);
            }

            @Test
            void getNonExistingObject() {
                //when
                assertThrows(NoSuchElementException.class,()-> personService.get(nonExistingUuid));
            }

            @Test
            void removeNonExistingObject() {
                //when
                assertThrows(NoSuchElementException.class,()-> personService.remove(nonExistingUuid));
            }

            @Test
            void updateNonExistingObject() {
                //when
                assertThrows(NoSuchElementException.class,()-> personService.update(nonExistingUuid, new PersonDTO()));
            }
        }
    }
    
    

}

 