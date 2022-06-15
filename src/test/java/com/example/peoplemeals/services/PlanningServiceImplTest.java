package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.PersonMapper;
import com.example.peoplemeals.api.v1.mapper.PlanningMapper;
import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.api.v1.model.PersonDTOList;
import com.example.peoplemeals.api.v1.model.PlanningDTO;
import com.example.peoplemeals.api.v1.model.forms.AssociateForm;
import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.domain.Planning;
import com.example.peoplemeals.domain.Restaurant;
import com.example.peoplemeals.helpers.PojoExampleCreation;
import com.example.peoplemeals.repositories.DishRepository;
import com.example.peoplemeals.repositories.PersonRepository;
import com.example.peoplemeals.repositories.PlanningRepository;
import com.example.peoplemeals.repositories.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Disabled
@ExtendWith(MockitoExtension.class)
class PlanningServiceImplTest {
    /* Expected functionalities:
•	OK: Associate, remove a person to a dish on a specific day (planning/meal)
•	OK: List people for a restaurant on a specific day (planning day)
•	OK: List people for a specific dish on a specific day (planning/meals)
•	OK: People who do not have dishes assigned on a specific day
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
    void associateAPersonToDishRestaurantAndDay() {
        //given data
        AssociateForm associateForm = new AssociateForm()
                .withPersonId(1L)
//                .withDishId(10L)
                .withRestaurantId(5L)
                .withDayOfWeek(DayOfWeek.MONDAY.toString());
        //given stubbing
        when(planningRepository.findAll()).thenReturn(new ArrayList<>());
//        when(dishRepository.findById(associateForm.getDishId())).thenReturn(Optional.of(new Dish()));
        when(personRepository.findById(associateForm.getPersonId())).thenReturn(Optional.of(new Person()));
        when(restaurantRepository.findById(associateForm.getRestaurantId())).thenReturn(Optional.of(new Restaurant()));
        //when
        PlanningDTO planningDTO = planningService.associate(associateForm);
        //then
//        verify(dishRepository).findById(associateForm.getDishId());
        verify(personRepository).findById(associateForm.getPersonId());
        verify(restaurantRepository).findById(associateForm.getRestaurantId());

        verify(planningRepository, times(2)).findAll(); //two validations need this
        verify(planningRepository).save(any(Planning.class));
        verify(planningMapper).planningToPlanningDTO(any(Planning.class));
    }
    @Test
    void disassociateAPersonToDishRestaurantAndDay() {
        //given data
        AssociateForm associateForm = new AssociateForm()
                .withPersonId(1L)
//                .withDishId(10L)
                .withRestaurantId(15L)
                .withDayOfWeek(DayOfWeek.MONDAY.toString());
        //given stubbing
        when(planningRepository.findAll()).thenReturn(new ArrayList<>(List.of(new Planning()
                .withPerson(new Person().withId(associateForm.getPersonId()))
//                .withDish(new Dish().withId(associateForm.getDishId()))
                .withRestaurant(new Restaurant().withId(associateForm.getRestaurantId()))
                .withDayOfWeek(DayOfWeek.valueOf(associateForm.getDayOfWeek())))));
//        when(dishRepository.findById(associateForm.getDishId())).thenReturn(Optional.of(new Dish()));
        when(personRepository.findById(associateForm.getPersonId())).thenReturn(Optional.of(new Person()));
        when(restaurantRepository.findById(associateForm.getRestaurantId())).thenReturn(Optional.of(new Restaurant()));
        //when
        PlanningDTO planningDTO = planningService.disassociate(associateForm);
        //then
//        verify(dishRepository).findById(associateForm.getDishId());
        verify(personRepository).findById(associateForm.getPersonId());
        verify(restaurantRepository).findById(associateForm.getRestaurantId());

        verify(planningRepository).findAll();
        verify(planningRepository).delete(any(Planning.class));
        verify(planningMapper).planningToPlanningDTO(any(Planning.class));
    }
    @Test
    void getPersonListByRestaurantAndDay() {
        //given data - Plannings with Dishes that belongs to a Restaurant
        Restaurant restaurant = PojoExampleCreation.createRestaurantExample(1);
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        Planning planning1 = PojoExampleCreation.createPlanningExample(2);
        planning1.setDayOfWeek(dayOfWeek); planning1.setRestaurant(restaurant);
        Planning planning2 = PojoExampleCreation.createPlanningExample(3);
        planning2.setDayOfWeek(dayOfWeek);  planning2.setRestaurant(restaurant);
        //given stubbing
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(planningRepository.findAll()).thenReturn(new ArrayList<>(List.of(planning1,planning2)));
        when(personMapper.personToPersonDTO(planning1.getPerson())).thenReturn(new PersonDTO().withId(planning1.getPerson().getId()));
        when(personMapper.personToPersonDTO(planning2.getPerson())).thenReturn(new PersonDTO().withId(planning2.getPerson().getId()));
        //when
        PersonDTOList personDTOList = planningService.getPersonListByRestaurantAndDay(restaurant.getId(),dayOfWeek.toString());
        //then
        verify(planningRepository).findAll();
        verify(restaurantRepository).findById(restaurant.getId());
        verify(personMapper,times(2)).personToPersonDTO(any(Person.class));
        assertEquals(2,personDTOList.getPersonDTOList().size());
    }
    @Test
    void getPersonListByDishAndDay() {
        //given data - Plannings with same Dishes
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        Planning planning1 = PojoExampleCreation.createPlanningExample(1);
        planning1.setDayOfWeek(dayOfWeek);
        Planning planning2 = PojoExampleCreation.createPlanningExample(2);
        planning2.setDayOfWeek(dayOfWeek);
        long dishId = planning1.getDish().getId();
        //given stubbing
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(planning1.getDish()));
        when(planningRepository.findAll()).thenReturn(new ArrayList<>(List.of(planning1,planning2)));
        when(personMapper.personToPersonDTO(planning1.getPerson())).thenReturn(new PersonDTO().withId(planning1.getPerson().getId()));
        //when
        PersonDTOList personDTOList = planningService.getPersonListByDishAndDay(dishId,dayOfWeek.toString());
        //then
        verify(dishRepository).findById(dishId);
        verify(planningRepository).findAll();
        verify(personMapper).personToPersonDTO(any(Person.class));
        assertEquals(1,personDTOList.getPersonDTOList().size());
    }
    @Test
    void getPersonListWithNoDishByDay() {
        //given data - Two Persons, one is without Planning for this Day
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        Planning planning1 = PojoExampleCreation.createPlanningExample(1);
        planning1.setDayOfWeek(dayOfWeek);
        Person personWithoutDish = PojoExampleCreation.createPersonExample(2);
        PersonDTO personDTOWithoutDish = PojoExampleCreation.createPersonDTOExample(2);
        //given stubbing
        when(planningRepository.findAll()).thenReturn(new ArrayList<>(List.of(planning1)));
        when(personRepository.findAll()).thenReturn(List.of(planning1.getPerson(),personWithoutDish));
        when(personMapper.personToPersonDTO(personWithoutDish)).thenReturn(personDTOWithoutDish);
        //when
        PersonDTOList personDTOList = planningService.getPersonListWithNoDishByDay(dayOfWeek.toString());
        //then
        verify(planningRepository).findAll();
        verify(personRepository).findAll();
        verify(personMapper,times(1)).personToPersonDTO(any(Person.class));
        assertEquals(1,personDTOList.getPersonDTOList().size());
    }
}