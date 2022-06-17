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
import java.util.UUID;

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
        private final String PERSON_UUID = "uuid_example";

        @Test
        void removeAnExistingObject() {
            //given
            long idExample = 15L;
            when(personRepository.findIdRequiredByUuid(anyString())).thenReturn(idExample);
            //when
            personService.remove(PERSON_UUID);
            //then
            verify(personRepository).deleteById(idExample);
            verify(personRepository).findIdRequiredByUuid(PERSON_UUID);
        }

        @Nested
        class GetAndGetAllMethods {
            @Test
            void getAllElements() {
                when(personRepository.findAll()).thenReturn(List.of(new Person()));
                //when
                personService.getAll();
                //then
                verify(personRepository).findAll();
            }

            @Test
            void getASingleElement() {
                //given
                when(personRepository.findRequiredByUuid(anyString())).thenReturn((new Person()));
                //when
                personService.get(PERSON_UUID);
                //then
                verify(personRepository).findRequiredByUuid(PERSON_UUID);
            }
            @AfterEach
            void checkCommonAsserts() {
                //then
                verify(personMapper,times(1)).personToPersonDTO(any(Person.class));
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
                String personUuid = UUID.randomUUID().toString();
                long personIdFromDB = 15L;
                when(personRepository.findIdRequiredByUuid(personUuid)).thenReturn((
                        personIdFromDB));
                //when
                PersonDTO personUpdatedDTO = personService.update(personUuid,new PersonDTO());
                //then
                verify(personRepository).findIdRequiredByUuid(personUuid);       //Entity ID is fetched by UUID
                verify(personRepository).save(personArgumentCaptor.capture());  //Entity is updated

                Person personCaptured = personArgumentCaptor.getValue();      //Capture the object saved
                assertEquals(personIdFromDB,personCaptured.getId());   //Assert that ID was the same as fetched before persisted
                assertEquals(personUuid,personCaptured.getUuid().toString());   //Assert that UUID was the same as fetched before persisted
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
        void anyServiceWithNullObjects() {
            //given expected behavior
            when(personRepository.findRequiredByUuid(null)).thenThrow(IllegalArgumentException.class);
            when(personRepository.findIdRequiredByUuid(null)).thenThrow(IllegalArgumentException.class);
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

            @Test
            void getNonExistingObject() {
                //given expected behavior
                when(personRepository.findRequiredByUuid(nonExistingUuid)).thenThrow(NoSuchElementException.class);
                //when
                assertThrows(NoSuchElementException.class,()-> personService.get(nonExistingUuid));
            }
            @Nested
            class removeAndUpdateMethods {
                @BeforeEach
                void stubbingExpectedBehaviours() {
                    when(personRepository.findIdRequiredByUuid(nonExistingUuid)).thenThrow(NoSuchElementException.class);
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
    
    

}

 