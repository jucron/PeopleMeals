package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import testUtils.PojoExampleCreation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static testUtils.JsonConverter.asJsonString;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {
    private final String BASE_URL = "/persons/";

    @Mock
    private PersonService personService;

    private MockMvc mockMvc;
    /** Expected endpoints:
     •	Add, remove, edit Persons (CRUD)
     •   v2: get, getAll
     NOTE: Tested in deeper layers: Wrong Format requests; Element Not In DB
     */

    @InjectMocks
    private PersonController personController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
    }

    @Nested
    class CorrectRequests {
        //given
        private final PersonDTO PERSON_DTO = PojoExampleCreation.createPersonDTOExample(1);

        @Test
        void getAllPersonsFromRepo() throws Exception {
            //when
            mockMvc.perform(get(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            verify(personService).getAll();
        }

        @Test
        void getAPersonFromRepo() throws Exception {
            //when
            mockMvc.perform(get(BASE_URL + PERSON_DTO.getUuid().toString())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            verify(personService).get(PERSON_DTO.getUuid().toString());

        }

        @Test
        void addAPersonToRepository() throws Exception {
            //when
            mockMvc.perform(post(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(PERSON_DTO)))
                    .andExpect(status().isCreated());
            verify(personService,times(1)).add(PERSON_DTO);
        }

        @Test
        void removeAPersonFromRepo() throws Exception {
            //when
            mockMvc.perform(delete(BASE_URL + PERSON_DTO.getUuid().toString())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            verify(personService).remove(PERSON_DTO.getUuid().toString());
        }

        @Test
        void updateAPersonFromRepo() throws Exception {
            //given
            Person person = PojoExampleCreation.createPersonExample(2);
            //when
            mockMvc.perform(put(BASE_URL + person.getUuid())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(PERSON_DTO)))
                    .andExpect(status().isOk());
            verify(personService).update(person.getUuid().toString(),PERSON_DTO);
        }
    }

    @Nested
    class FailRequests {

        @Test
        void emptyBody_Add() throws Exception {
            //when
            mockMvc.perform(post(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
            verify(personService,times(0)).add(any());
        }

        @Test
        void emptyUuid_Delete() throws Exception {
            //when
            mockMvc.perform(delete(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isMethodNotAllowed());
            verify(personService,times(0)).remove(any());
        }

        @Nested
        class FailUpdate {
            @Test
            void emptyBodyAndUuid() throws Exception {

                //when
                mockMvc.perform(put(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isMethodNotAllowed());
                verify(personService,times(0)).update(any(), any());
            }

            @Test
            void emptyUuid() throws Exception {

                //when
                mockMvc.perform(put(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(new PersonDTO())))
                        .andExpect(status().isMethodNotAllowed());
                verify(personService,times(0)).update(any(), any());
            }

            @Test
            void emptyBody() throws Exception {

                //when
                mockMvc.perform(put(BASE_URL+"Some-uuid")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
                verify(personService,times(0)).update(any(), any());
            }
        }
    }

}