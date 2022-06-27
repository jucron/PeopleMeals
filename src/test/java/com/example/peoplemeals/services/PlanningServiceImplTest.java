package com.example.peoplemeals.services;


import com.example.peoplemeals.api.v1.mapper.PersonMapper;
import com.example.peoplemeals.api.v1.mapper.PlanningMapper;
import com.example.peoplemeals.api.v1.model.forms.AssociateForm;
import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.domain.Planning;
import com.example.peoplemeals.domain.Restaurant;
import com.example.peoplemeals.helpers.DayOfWeekHelper;
import com.example.peoplemeals.repositories.DishRepository;
import com.example.peoplemeals.repositories.PersonRepository;
import com.example.peoplemeals.repositories.PlanningRepository;
import com.example.peoplemeals.repositories.RestaurantRepository;
import com.example.peoplemeals.repositories.validations.PlanningValidation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    @Mock
    private PlanningValidation planningValidation;
    @Captor
    private ArgumentCaptor<Planning> planningCaptor;

    @BeforeEach
    public void setUpForAll() {
        //Instantiate service class
        planningService = new PlanningServiceImpl(
                planningRepository, planningMapper, planningValidation, dishRepository,
                personRepository, restaurantRepository, personMapper);
    }

    @Nested
    class SuccessfulServices {
        @Nested
        class GetAndGetAllMethods {
            @Test
            void getElements() {
                when(planningRepository.findAll()).thenReturn(List.of(new Planning()));
                //when
                planningService.getAll();
                //then
                verify(planningRepository).findAll();
            }

            @Test
            void getASingleElement() {
                //given
                String someUuid = "example_uuid";
                when(planningRepository.findRequiredByUuid(someUuid)).thenReturn((new Planning()));
                //when
                planningService.get(someUuid);
                //then
                verify(planningRepository).findRequiredByUuid(someUuid);
            }

            @AfterEach
            void checkCommonAsserts() {
                //then
                verify(planningMapper, times(1)).planningToPlanningDTO(any(Planning.class));
            }
        }

        @Nested
        class AssociateAndDisassociate {
            //given common data
            long dishId = 1L;
            long personId = 10L;
            long restaurantId = 50L;
            AssociateForm form = new AssociateForm()
                    .withDishUuid("dish_uuid")
                    .withPersonUuid("person_uuid")
                    .withRestaurantUuid("restaurant_uuid")
                    .withDayOfWeek(DayOfWeek.MONDAY.toString());
            DayOfWeek dayOfWeekFormat = DayOfWeekHelper.validateDayOfWeek(form.getDayOfWeek());

            @BeforeEach
            void commonStubs() {
                //given stubbing
                when(dishRepository.findIdRequiredByUuid(form.getDishUuid())).thenReturn((dishId));
                when(personRepository.findIdRequiredByUuid(form.getPersonUuid())).thenReturn((personId));
                when(restaurantRepository.findIdRequiredByUuid(form.getRestaurantUuid())).thenReturn((restaurantId));
            }

            @Test
            void associateAPersonToDishPersonRestaurantAndDay() {
                when(dishRepository.getById(dishId)).thenReturn(new Dish().withId(dishId));
                when(personRepository.getById(personId)).thenReturn(new Person().withId((personId)));
                when(restaurantRepository.getById(restaurantId)).thenReturn(new Restaurant().withId((restaurantId)));
                //when
                planningService.associate(form);
                //then
                verify(dishRepository).findIdRequiredByUuid(form.getDishUuid());
                verify(personRepository).findIdRequiredByUuid(form.getPersonUuid());
                verify(restaurantRepository).findIdRequiredByUuid(form.getRestaurantUuid());
                verify(planningValidation).validateNoPlanForThisPersonInDayOfWeek(personId, dayOfWeekFormat);
                verify(planningValidation).validateLessThan15RestaurantsInDayOfWeek(restaurantId, dayOfWeekFormat);
                verify(planningMapper).planningToPlanningDTO(any());
                verify(planningRepository).save(planningCaptor.capture());

                verify(dishRepository).getById(dishId);
                verify(personRepository).getById(personId);
                verify(restaurantRepository).getById(restaurantId);
                //checking if all values were passed to the Planning saved:
                Planning planningSaved = planningCaptor.getValue();
                assertNotNull(planningSaved.getUuid());
                assertEquals(dishId, planningSaved.getDish().getId());
                assertEquals(personId, planningSaved.getPerson().getId());
                assertEquals(restaurantId, planningSaved.getRestaurant().getId());
                assertEquals(dayOfWeekFormat, planningSaved.getDayOfWeek());
            }

            @Test
            void disassociateAPersonFromPlanningRepo() {
                //given stub
                long planningId = 75L;
                when(planningRepository.findPlanningIdRequiredByFields(dishId, personId, restaurantId, dayOfWeekFormat))
                        .thenReturn(planningId); //must check if this ID will be used to delete
                //when
                planningService.disassociate(form);
                //then
                verify(dishRepository).findIdRequiredByUuid(form.getDishUuid());
                verify(personRepository).findIdRequiredByUuid(form.getPersonUuid());
                verify(restaurantRepository).findIdRequiredByUuid(form.getRestaurantUuid());

                verify(planningRepository).findPlanningIdRequiredByFields(dishId, personId, restaurantId, dayOfWeekFormat);
                verify(planningRepository).deleteById(planningId);
            }
        }

        @Nested
        class GetPersonLists {
            //given common data
            long dishId = 1L;
            long personId = 10L;
            long restaurantId = 50L;
            String dishUuid = "dish_uuid";
            String restaurantUuid = "restaurant_uuid";
            DayOfWeek dayOfWeekFormat = DayOfWeek.MONDAY;
            String dayOfWeek = dayOfWeekFormat.toString();

            @Test
            void getPersonListByRestaurantAndDay() {
                //given
                when(restaurantRepository.findIdRequiredByUuid(restaurantUuid)).thenReturn((restaurantId));
                when(planningRepository.findPersonsByRestaurantAndDayOfWeek(restaurantId, dayOfWeekFormat))
                        .thenReturn(new ArrayList<>(List.of(new Person())));
                //when
                planningService.getPersonListByRestaurantAndDay(restaurantUuid, dayOfWeek);
                //then
                verify(restaurantRepository).findIdRequiredByUuid(restaurantUuid);
                verify(planningRepository).findPersonsByRestaurantAndDayOfWeek(restaurantId, dayOfWeekFormat);
            }

            @Test
            void getPersonListByDishAndDay() {
                //given
                when(dishRepository.findIdRequiredByUuid(dishUuid)).thenReturn((dishId));
                when(planningRepository.findPersonsByDishAndDayOfWeek(dishId, dayOfWeekFormat))
                        .thenReturn(new ArrayList<>(List.of(new Person())));
                //when
                planningService.getPersonListByDishAndDay(dishUuid, dayOfWeek);
                //then
                verify(dishRepository).findIdRequiredByUuid(dishUuid);
                verify(planningRepository).findPersonsByDishAndDayOfWeek(dishId, dayOfWeekFormat);
            }

            @Test
            void getPersonListWithNoDishByDay() {
                //given
                List<Long> personsIDs = new ArrayList<>(List.of(personId));
                when(planningRepository.findPersonIDsByDayOfWeek(dayOfWeekFormat))
                        .thenReturn(personsIDs);
                when(personRepository.findAllNotInList(personsIDs))
                        .thenReturn(new ArrayList<>(List.of(new Person())));
                //when
                planningService.getPersonListWithNoDishByDay(dayOfWeek);
                //then
                verify(planningRepository).findPersonIDsByDayOfWeek(dayOfWeekFormat);
                verify(personRepository).findAllNotInList(personsIDs);
            }

            @AfterEach
            void commonChecks() {
                verify(personMapper).personToPersonDTO(any(Person.class));
            }

        }

    }

    @Nested
    class FailedServices {
        @Test
        void anyServiceWithNullObjects() {
            //given expected behavior
            String someParameter = "some_parameter";
            when(planningRepository.findRequiredByUuid(null)).thenThrow(IllegalArgumentException.class);

            //when-then
            assertThrows(IllegalArgumentException.class, () -> planningService.get(null));
            assertThrows(NullPointerException.class, () -> planningService.associate(null));
            assertThrows(NullPointerException.class, () -> planningService.disassociate(null));

            assertThrows(IllegalArgumentException.class, () -> planningService.getPersonListByRestaurantAndDay(null, null));
            assertThrows(IllegalArgumentException.class, () -> planningService.getPersonListByRestaurantAndDay(someParameter, null));
            assertThrows(IllegalArgumentException.class, () -> planningService.getPersonListByRestaurantAndDay(null, someParameter));

            assertThrows(IllegalArgumentException.class, () -> planningService.getPersonListByDishAndDay(null, null));
            assertThrows(IllegalArgumentException.class, () -> planningService.getPersonListByDishAndDay(someParameter, null));
            assertThrows(IllegalArgumentException.class, () -> planningService.getPersonListByDishAndDay(null, someParameter));

            assertThrows(IllegalArgumentException.class, () -> planningService.getPersonListWithNoDishByDay(null));
        }

        @Nested
        class AssociationValidationsNotPassing {
            private final DayOfWeek dayOfWeekFormat = DayOfWeek.MONDAY;

            @Test
            void validateNoPlanForThisPersonInDayOfWeek() {
                //given expected behavior
                long personId = 1L;
                String personUuid = "person_uuid";
                when(planningValidation.validateNoPlanForThisPersonInDayOfWeek(personId, dayOfWeekFormat))
                        .thenThrow(RuntimeException.class);
                when(personRepository.findIdRequiredByUuid(personUuid)).thenReturn(personId);
                //when and then
                assertThrows(RuntimeException.class, () -> {
                    planningService.associate(new AssociateForm()
                            .withPersonUuid(personUuid)
                            .withDayOfWeek(dayOfWeekFormat.toString()));
                });
            }

            @Test
            void validateLessThan15RestaurantsInDayOfWeek() {
                //given expected behavior
                long restaurantId = 1L;
                String restaurantUuid = "restaurant_uuid";
                when(planningValidation.validateLessThan15RestaurantsInDayOfWeek(restaurantId, dayOfWeekFormat))
                        .thenThrow(RuntimeException.class);
                when(restaurantRepository.findIdRequiredByUuid(restaurantUuid)).thenReturn(restaurantId);
                //when and then
                assertThrows(RuntimeException.class, () -> {
                    planningService.associate(new AssociateForm()
                            .withRestaurantUuid(restaurantUuid)
                            .withDayOfWeek(dayOfWeekFormat.toString()));
                });
            }
        }

        @Nested
        class AccessingNonExistingObjectsInDatabase {
            private final String nonExistingUuid = "uuid-example";
            private final String dayOfWeek = DayOfWeek.MONDAY.toString();

            @Test
            void getNonExistingObject() {
                //given expected behavior
                when(planningRepository.findRequiredByUuid(nonExistingUuid)).thenThrow(NoSuchElementException.class);
                //when
                assertThrows(NoSuchElementException.class, () -> planningService.get(nonExistingUuid));
            }

            @Nested
            class associateMethod {
                @Test
                void nonExistingDish() {
                    //given expected behavior
                    when(dishRepository.findIdRequiredByUuid(nonExistingUuid)).thenThrow(NoSuchElementException.class);
                    //when
                    assertThrows(NoSuchElementException.class, () -> planningService.associate(
                            new AssociateForm().withDishUuid(nonExistingUuid).withDayOfWeek(dayOfWeek)));
                }

                @Test
                void nonExistingPerson() {
                    //given expected behavior
                    when(personRepository.findIdRequiredByUuid(nonExistingUuid)).thenThrow(NoSuchElementException.class);
                    //when
                    assertThrows(NoSuchElementException.class, () -> planningService.associate(
                            new AssociateForm().withPersonUuid(nonExistingUuid).withDayOfWeek(dayOfWeek)));
                }

                @Test
                void nonExistingRestaurant() {
                    //given expected behavior
                    when(restaurantRepository.findIdRequiredByUuid(nonExistingUuid)).thenThrow(NoSuchElementException.class);
                    //when
                    assertThrows(NoSuchElementException.class, () -> planningService.associate(
                            new AssociateForm().withRestaurantUuid(nonExistingUuid).withDayOfWeek(dayOfWeek)));
                }
            }

            @Nested
            class disassociateMethod {
                @Test
                void nonExistingDish() {
                    //given expected behavior
                    when(dishRepository.findIdRequiredByUuid(nonExistingUuid)).thenThrow(NoSuchElementException.class);
                    //when
                    assertThrows(NoSuchElementException.class, () -> planningService.disassociate(
                            new AssociateForm().withDishUuid(nonExistingUuid).withDayOfWeek(dayOfWeek)));
                }

                @Test
                void nonExistingPerson() {
                    //given expected behavior
                    when(personRepository.findIdRequiredByUuid(nonExistingUuid)).thenThrow(NoSuchElementException.class);
                    //when
                    assertThrows(NoSuchElementException.class, () -> planningService.disassociate(
                            new AssociateForm().withPersonUuid(nonExistingUuid).withDayOfWeek(dayOfWeek)));
                }

                @Test
                void nonExistingRestaurant() {
                    //given expected behavior
                    when(restaurantRepository.findIdRequiredByUuid(nonExistingUuid)).thenThrow(NoSuchElementException.class);
                    //when
                    assertThrows(NoSuchElementException.class, () -> planningService.disassociate(
                            new AssociateForm().withRestaurantUuid(nonExistingUuid).withDayOfWeek(dayOfWeek)));
                }

                @Test
                void nonExistingPlanning() {
                    //given expected behavior
                    String dishUuid = "dish_uuid";
                    String personUuid = "person_uuid";
                    String restaurantUuid = "restaurant_uuid";
                    long dishId = 1L;
                    long personId = 10L;
                    long restaurantId = 50L;
                    when(dishRepository.findIdRequiredByUuid(dishUuid)).thenReturn(dishId);
                    when(personRepository.findIdRequiredByUuid(personUuid)).thenReturn(personId);
                    when(restaurantRepository.findIdRequiredByUuid(restaurantUuid)).thenReturn(restaurantId);
                    when(planningRepository.findPlanningIdRequiredByFields(
                            dishId, personId, restaurantId, DayOfWeek.valueOf(dayOfWeek)))
                            .thenThrow(NoSuchElementException.class);
                    //when
                    assertThrows(NoSuchElementException.class, () -> planningService.disassociate(
                            new AssociateForm()
                                    .withDishUuid(dishUuid)
                                    .withPersonUuid(personUuid)
                                    .withRestaurantUuid(restaurantUuid)
                                    .withDayOfWeek(dayOfWeek)));
                }
            }

            @Nested
            class getPersonListByRestaurantAndDay {
                @Test
                void nonExistingRestaurant() {
                    //given
                    String restaurantUuid = "restaurant_uuid";
                    when(restaurantRepository.findIdRequiredByUuid(restaurantUuid)).thenThrow(NoSuchElementException.class);
                    //when
                    assertThrows(NoSuchElementException.class, () -> planningService.
                            getPersonListByRestaurantAndDay(restaurantUuid, dayOfWeek));
                }
            }

            @Nested
            class getPersonListByDishAndDay {
                @Test
                void nonExistingDish() {
                    //given
                    String dishId = "dish_uuid";
                    when(dishRepository.findIdRequiredByUuid(dishId)).thenThrow(NoSuchElementException.class);
                    //when
                    assertThrows(NoSuchElementException.class, () -> planningService.
                            getPersonListByDishAndDay(dishId, dayOfWeek));
                }
            }
        }
    }
}
