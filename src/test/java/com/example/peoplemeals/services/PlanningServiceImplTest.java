package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.PersonMapper;
import com.example.peoplemeals.api.v1.mapper.PlanningMapper;
import com.example.peoplemeals.api.v1.model.PersonDTOList;
import com.example.peoplemeals.api.v1.model.PlanningDTO;
import com.example.peoplemeals.api.v1.model.forms.AssociateForm;
import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.domain.Planning;
import com.example.peoplemeals.repositories.DishRepository;
import com.example.peoplemeals.repositories.PersonRepository;
import com.example.peoplemeals.repositories.PlanningRepository;
import com.example.peoplemeals.repositories.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanningServiceImplTest {
    /* Expected functionalities:
•	OK: Associate, remove a person to a dish on a specific day (planning/meal)
•	Todo: List people for a restaurant on a specific day (planning day)
•	Todo: List people for a specific dish on a specific day (planning/meals)
•	Todo: People who do not have dishes assigned on a specific day
     */
    private PlanningService planningService;

    @Mock
    private PlanningRepository planningRepository;
    @Mock
    private PlanningMapper planningMapper;
    @Mock
    private DishRepository dishRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private PersonMapper personMapper;

    @BeforeEach
    public void setUp(){
        planningService = new PlanningServiceImpl(
                planningRepository,planningMapper,dishRepository,
                personRepository,restaurantRepository,personMapper);
    }
    @Test
    void associateAPersonByDishAndDay() {
        //given
        AssociateForm associateForm = new AssociateForm()
                .withPersonId(1L)
                .withDishId(10L)
                .withDayOfWeek(DayOfWeek.MONDAY)
                .withRemove(false);
        when(planningRepository.findAll()).thenReturn(new ArrayList<>());
        when(dishRepository.findById(associateForm.getDishId())).thenReturn(Optional.of(new Dish()));
        when(personRepository.findById(associateForm.getPersonId())).thenReturn(Optional.of(new Person()));
        //when
        PlanningDTO planningDTO = planningService.associate(associateForm);
        //then
        verify(planningRepository).findAll();
        verify(planningRepository).save(any(Planning.class));
        verify(planningMapper).planningToPlanningDTO(any(Planning.class));
    }
    @Test
    void removeAPersonByDishAndDay() {
        //given
        AssociateForm associateForm = new AssociateForm()
                .withPersonId(1L)
                .withDishId(10L)
                .withDayOfWeek(DayOfWeek.MONDAY)
                .withRemove(true);
        when(planningRepository.findAll()).thenReturn(new ArrayList<>(List.of(new Planning()
                .withPerson(new Person().withId(associateForm.getPersonId()))
                .withDish(new Dish().withId(associateForm.getDishId()))
                .withDayOfWeek(associateForm.getDayOfWeek()))));
         //when
        PlanningDTO planningDTO = planningService.associate(associateForm);
        //then
        verify(planningRepository).findAll();
        verify(planningRepository).delete(any(Planning.class));
        verify(planningMapper).planningToPlanningDTO(any(Planning.class));
    }
    @Test
    void getPersonListByRestaurantAndDay() {
        //given
        long restaurantId = 1L;
        String dayOfWeek = DayOfWeek.MONDAY.toString();
        //when
        PersonDTOList personDTOList = planningService.getPersonListByRestaurantAndDay(restaurantId,dayOfWeek);
        //then
        assertEquals(2,personDTOList.getPersonDTOList().size());
    }
    @Test
    void getPersonListByDishAndDay() {
        fail();
    }
    @Test
    void getPersonListWithNoDishByDay() {
        fail();
    }
}