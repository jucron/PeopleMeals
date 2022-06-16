package com.example.peoplemeals.controllers;

import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.api.v1.model.PlanningDTO;
import com.example.peoplemeals.api.v1.model.forms.AssociateForm;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;
import com.example.peoplemeals.services.PlanningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import java.util.UUID;

import static com.example.peoplemeals.helpers.JsonConverter.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@ExtendWith(MockitoExtension.class)
class PlanningControllerTest {
    /* Expected functionalities:
•	OK: Associate, remove a person to a dish on a specific day (planning/meal)
•	OK: List people for a restaurant on a specific day (planning day)
•	OK: List people for a specific dish on a specific day (planning/meals)
•	OK: People who do not have dishes assigned on a specific day
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
    void associateAPersonToDishRestaurantAndDay() throws Exception {
        //given
        AssociateForm associateForm = new AssociateForm()
                .withDishUuid("uuid-dish")
                .withPersonId(5L)
                .withRestaurantId(10L)
                .withDayOfWeek(DayOfWeek.MONDAY.toString());
        when(planningService.associate(associateForm)).thenReturn(new PlanningDTO()
                        .withDishDTO(new DishDTO().withUuid(UUID.fromString(associateForm.getDishUuid())))
                        .withPersonDTO(new PersonDTO().withId(associateForm.getPersonId()))
                        .withDayOfWeek(DayOfWeek.valueOf(associateForm.getDayOfWeek())));
        //when
        mockMvc.perform(post(BASE_URL + "/associate")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(associateForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dishDTO.uuid", equalTo(associateForm.getDishUuid())))
                .andExpect(jsonPath("$.personDTO.id", equalTo((int)associateForm.getPersonId())))
                .andExpect(jsonPath("$.dayOfWeek", equalTo(associateForm.getDayOfWeek())));
        verify(planningService).associate(associateForm);
    }
    @Test
    void disassociateAPersonToDishRestaurantAndDay() throws Exception {
        //given
        AssociateForm associateForm = new AssociateForm()
                .withDishUuid("uuid-dish")
                .withPersonId(5L)
                .withRestaurantId(10L)
                .withDayOfWeek(DayOfWeek.MONDAY.toString());
        when(planningService.disassociate(associateForm)).thenReturn(new PlanningDTO()
                        .withId(null)
                        .withDishDTO(new DishDTO().withUuid(UUID.fromString(associateForm.getDishUuid())))
                        .withPersonDTO(new PersonDTO().withId(associateForm.getPersonId()))
                        .withDayOfWeek(DayOfWeek.valueOf(associateForm.getDayOfWeek())));
        //when
        mockMvc.perform(post(BASE_URL + "/disassociate")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(associateForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(null)))
                .andExpect(jsonPath("$.dishDTO.uuid", equalTo((associateForm.getDishUuid()))))
                .andExpect(jsonPath("$.personDTO.id", equalTo((int)associateForm.getPersonId())))
                .andExpect(jsonPath("$.dayOfWeek", equalTo(associateForm.getDayOfWeek())));
        verify(planningService).disassociate(associateForm);
    }
    @Test
    void getPersonDTOListByRestaurantAndDayOfWeek() throws Exception {
        //given
        long restaurantId = 1L;
        String dayOfWeek = DayOfWeek.MONDAY.toString();
        EntityDTOList<PersonDTO> personDTOList = new EntityDTOList<PersonDTO>().withEntityDTOList(new HashSet<>(Set.of(
                new PersonDTO().withId(5L), new PersonDTO().withId(10L))));
        when(planningService.getPersonListByRestaurantAndDay(restaurantId, dayOfWeek))
                .thenReturn(personDTOList);
        //when
        mockMvc.perform(get(BASE_URL + "/getPersonList/"+restaurantId+"/"+dayOfWeek+"/restaurant")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personDTOList", hasSize(2)));

        verify(planningService).getPersonListByRestaurantAndDay(restaurantId, dayOfWeek);
    }
    @Test
    void getPersonDTOListByDishAndDayOfWeek() throws Exception {
        //given
        long dishId = 1L;
        String dayOfWeek = DayOfWeek.MONDAY.toString();
        EntityDTOList<PersonDTO> personDTOList = new EntityDTOList<PersonDTO>().withEntityDTOList(new HashSet<>(Set.of(
                new PersonDTO().withId(5L), new PersonDTO().withId(10L))));
        when(planningService.getPersonListByDishAndDay(dishId, dayOfWeek))
                .thenReturn(personDTOList);
        //when
        mockMvc.perform(get(BASE_URL + "/getPersonList/"+dishId+"/"+dayOfWeek+"/dish")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personDTOList", hasSize(2)));
        verify(planningService).getPersonListByDishAndDay(dishId, dayOfWeek);
    }
    @Test
    void getPersonDTOListWithNoDishByDayOfWeek() throws Exception {
        //given
        String dayOfWeek = DayOfWeek.MONDAY.toString();
        EntityDTOList<PersonDTO> personDTOList = new EntityDTOList<PersonDTO>().withEntityDTOList(new HashSet<>(Set.of(
                new PersonDTO().withId(5L), new PersonDTO().withId(10L))));
        when(planningService.getPersonListWithNoDishByDay(dayOfWeek))
                .thenReturn(personDTOList);
        //when
        mockMvc.perform(get(BASE_URL + "/getPersonList/"+dayOfWeek+"/no_dish")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personDTOList", hasSize(2)));
        verify(planningService).getPersonListWithNoDishByDay(dayOfWeek);
    }
}