package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.api.v1.model.PersonDTOList;
import com.example.peoplemeals.api.v1.model.PlanningDTO;
import com.example.peoplemeals.api.v1.model.forms.AssociateForm;
import com.example.peoplemeals.services.PlanningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

import static com.example.peoplemeals.helpers.JsonConverter.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PlanningControllerTest {
    /* Expected functionalities:
•	OK: Associate, remove a person to a dish on a specific day (planning/meal)
•	todo: List people for a restaurant on a specific day (planning day)
•	todo: List people for a specific dish on a specific day (planning/meals)
•	todo: People who do not have dishes assigned on a specific day
     */
    @InjectMocks
    private PlanningController planningController;
    @Mock
    private PlanningService planningService;

    private MockMvc mockMvc;
    private final String BASE_URL = PlanningController.BASE_URL;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(planningController).build();
    }
    @Test
    void associatePersonToDish() throws Exception {
        //given
        AssociateForm associateForm = new AssociateForm()
                .withDishId(1L)
                .withPersonId(5L)
                .withDayOfWeek(DayOfWeek.MONDAY)
                .withRemove(false);
        when(planningService.associate(associateForm)).thenReturn(new PlanningDTO()
                        .withDishDTO(new DishDTO().withId(associateForm.getDishId()))
                        .withPersonDTO(new PersonDTO().withId(associateForm.getPersonId()))
                        .withDayOfWeek(associateForm.getDayOfWeek()));
        //when
        mockMvc.perform(post(BASE_URL + "/associate")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(associateForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dishDTO.id", equalTo((int)associateForm.getDishId())))
                .andExpect(jsonPath("$.personDTO.id", equalTo((int)associateForm.getPersonId())))
                .andExpect(jsonPath("$.dayOfWeek", equalTo(associateForm.getDayOfWeek().toString())))
                ;
        verify(planningService).associate(associateForm);

    }
    @Test
    void getPersonDTOList() throws Exception {
        //given
        long restaurantId = 1L;
        String dayOfWeek = DayOfWeek.MONDAY.toString();
        PersonDTOList personDTOList = new PersonDTOList().withPersonDTOList(new HashSet<>(Set.of(
                new PersonDTO().withId(5L), new PersonDTO().withId(10L))));
        when(planningService.getPersonListByRestaurantAndDay(restaurantId, dayOfWeek))
                .thenReturn(personDTOList);
        //when
        mockMvc.perform(get(BASE_URL + "/getPersonList/"+restaurantId+"/"+dayOfWeek)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personDTOList", hasSize(2)));

        verify(planningService).getPersonListByRestaurantAndDay(restaurantId, dayOfWeek);

    }

}