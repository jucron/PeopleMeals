package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.helpers.PojoExampleCreation;
import com.example.peoplemeals.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.example.peoplemeals.helpers.RestConverter.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {
    /* Expected endpoints:
    •OK:	Add, remove, edit persons (CRUD)
     */

    @InjectMocks
    private PersonController personController;

    @Mock
    private PersonService personService;

    private MockMvc mockMvc;
    private final String BASE_URL = PersonController.BASE_URL;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
    }

    @Test
    void addAPersonToRepository() throws Exception {
        //given
        PersonDTO personDTO = PojoExampleCreation.createPersonDTOExample(1);
        given(personService.add(personDTO)).willReturn(personDTO);
        //when
        mockMvc.perform(post(BASE_URL + "/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(personDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName", equalTo(personDTO.getFullName())))
                .andExpect(jsonPath("$.fiscal", equalTo(personDTO.getFiscal())));

        verify(personService,times(1)).add(personDTO);
    }
    @Test
    void removeAPersonFromRepo() throws Exception {
        //given
        PersonDTO personDTO = PojoExampleCreation.createPersonDTOExample(1);
//        given(personService.remove(personDTO.getId())).willReturn(personDTO);
        //when
        mockMvc.perform(delete(BASE_URL + "/remove/"+personDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(personService).remove(personDTO.getId());
    }
    @Test
    void updateAPersonFromRepo() throws Exception {
        //given
        Person person = PojoExampleCreation.createPersonExample(1);
        PersonDTO personDTO = PojoExampleCreation.createPersonDTOExample(2);
        given(personService.update(person.getId(), personDTO)).willReturn(personDTO);

        //when
        mockMvc.perform(put(BASE_URL + "/update/"+person.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(personDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", equalTo(personDTO.getFullName())))
                .andExpect(jsonPath("$.fiscal", equalTo(personDTO.getFiscal())));
        verify(personService).update(person.getId(),personDTO);
    }

}