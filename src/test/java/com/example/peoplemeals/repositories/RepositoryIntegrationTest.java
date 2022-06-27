package com.example.peoplemeals.repositories;

import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.domain.Planning;
import com.example.peoplemeals.domain.Restaurant;
import com.example.peoplemeals.domain.security.Credentials;
import com.example.peoplemeals.domain.security.Role;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.NestedRuntimeException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import testUtils.PojoExampleCreation;

import java.time.DayOfWeek;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RepositoryIntegrationTest {

    @Autowired
    private com.example.peoplemeals.repositories.DishRepository dishRepository;
    @Autowired
    private com.example.peoplemeals.repositories.PlanningRepository planningRepository;
    @Autowired
    private com.example.peoplemeals.repositories.PersonRepository personRepository;
    @Autowired
    private com.example.peoplemeals.repositories.RestaurantRepository restaurantRepository;
    @Autowired
    private com.example.peoplemeals.repositories.CredentialsRepository credentialsRepository;

    private final UUID RANDOM_UUID = UUID.randomUUID();
    private Dish dishToBeTested;
    private Planning planningToBeTested;
    private Person personToBeTested;

    @BeforeAll
    void setUpDataInRepo() {
        int count = 1;
        //Populate Dishes repo:
        dishToBeTested = dishRepository.save(PojoExampleCreation.createDishExample(count++));
        Dish dish2 = dishRepository.save(PojoExampleCreation.createDishExample(count++));
        //Populate Persons repo:
        personToBeTested = personRepository.save(PojoExampleCreation.createPersonExample(count++));
        personRepository.save(PojoExampleCreation.createPersonExample(count++));
        //Populate Restaurant repo:
        Restaurant restaurant = PojoExampleCreation.createRestaurantExample(count++);
        restaurant.setDishes(Set.of(dishToBeTested, dish2));
        restaurant = restaurantRepository.save(restaurant);
        //Populate Planning repo:
        Planning planning = PojoExampleCreation.createPlanningExample(count);
        planning.setDish(dishToBeTested);
        planning.setPerson(personToBeTested);
        planning.setRestaurant(restaurant);
        planningToBeTested = planningRepository.save(planning);
        //Populate Credentials repo:
        credentialsRepository.save(
                new Credentials().withUsername("username").withPassword("password").withRole(Role.USER));
    }

    @Nested
    class PeopleMealsRepository {
        @Nested
        class findByUuid {
            @Test
            void exists() {
                Optional<Dish> dishOptionalFetched = dishRepository.findByUuid(dishToBeTested.getUuid());
                assertTrue(dishOptionalFetched.isPresent());
                assertEquals(dishToBeTested.getId(),dishOptionalFetched.get().getId());
                assertEquals(dishToBeTested.getUuid(),dishOptionalFetched.get().getUuid());
            }

            @Test
            void notInDB() {
                Optional<Dish> dishOptionalFetched = dishRepository.findByUuid(RANDOM_UUID);
                assertTrue(dishOptionalFetched.isEmpty());
            }

        }

        @Nested
        class findRequiredByUuid {
            @Test
            void exists() {
                Dish dishFetched = dishRepository.findRequiredByUuid(dishToBeTested.getUuid().toString());
                assertEquals(dishToBeTested.getId(),dishFetched.getId());
                assertEquals(dishToBeTested.getUuid(),dishFetched.getUuid());
            }

            @Test
            void notInDB() {
                assertThrows(NoSuchElementException.class,()->dishRepository.findRequiredByUuid(RANDOM_UUID.toString()));
            }

            @Test
            void inWrongFormat() {
                assertThrows(NestedRuntimeException.class,()->dishRepository.findRequiredByUuid("some_wrong_format"));
            }
        }

        @Nested
        class findIdByUuid {
            @Test
            void exists() {
                Optional<ProjectId> dishIdOptional = dishRepository.findIdByUuid(dishToBeTested.getUuid());
                assertTrue(dishIdOptional.isPresent());
                assertEquals(dishToBeTested.getId(), dishIdOptional.get().getId());
            }

            @Test
            void notInDB() {
                Optional<ProjectId> dishIdOptional = dishRepository.findIdByUuid(RANDOM_UUID);
                assertTrue(dishIdOptional.isEmpty());
            }
        }

        @Nested
        class findIdRequiredByUuid {
            @Test
            void exists() {
                long dishId = dishRepository.findIdRequiredByUuid(dishToBeTested.getUuid().toString());
                assertEquals(dishToBeTested.getId(),dishId);
            }

            @Test
            void notInDB() {
                assertThrows(NoSuchElementException.class,()->dishRepository.findIdRequiredByUuid(RANDOM_UUID.toString()));
            }

            @Test
            void inWrongFormat() {
                assertThrows(NestedRuntimeException.class,()->dishRepository.findIdRequiredByUuid("some_wrong_format"));
            }
        }
    }

    @Nested
    class PersonRepository {
        @Test
        void findAllNotInList() {
            //given
            long personId = personToBeTested.getId();
            //when
            //want to get list without the personToBeTested
            List<Person> personListFetched = personRepository.findAllNotInList(List.of(personId));
            //then
            assertEquals(1,personListFetched.size()); //total size of repo - list with one = (2-1) = 1.
            assertNotEquals(personId,personListFetched.get(0).getId()); //list fetched should not have person in list as parameter
        }
    }

    @Nested
    class PlanningRepository {
        private long dishId;
        private long restaurantId;
        private long personId;
        private DayOfWeek dayOfWeek;

        @BeforeEach
        void assignCommonValueToFields() {
            dishId = planningToBeTested.getDish().getId();
            restaurantId = planningToBeTested.getRestaurant().getId();
            personId = planningToBeTested.getPerson().getId();
            dayOfWeek = planningToBeTested.getDayOfWeek();
        }

        @Nested
        class findPlanningIdByFields {
            @Test
            void exists() {
                //when
                Optional<Long> planningIdFetched = planningRepository.findPlanningIdByFields(
                        dishId, personId, restaurantId, dayOfWeek);
                //then
                assertTrue(planningIdFetched.isPresent());
                assertEquals(planningToBeTested.getId(), planningIdFetched.get());
            }

            @Test
            void notInDB() {
                //when and then
                assertTrue((planningRepository.findPlanningIdByFields(dishId + 10, personId, restaurantId, dayOfWeek)).isEmpty());
                assertTrue((planningRepository.findPlanningIdByFields(dishId, personId + 10, restaurantId, dayOfWeek)).isEmpty());
                assertTrue((planningRepository.findPlanningIdByFields(dishId, personId, restaurantId + 10, dayOfWeek)).isEmpty());
                assertTrue((planningRepository.findPlanningIdByFields(dishId, personId, restaurantId, DayOfWeek.SUNDAY)).isEmpty());
            }
        }

        @Nested
        class findPlanningIdRequiredByFields {
            @Test
            void exists() {
                //when
                long planningId = planningRepository.findPlanningIdRequiredByFields(dishId,personId,restaurantId,dayOfWeek);
                //then
                assertEquals(planningToBeTested.getId(),planningId);
            }

            @Test
            void notInDB() {
                assertThrows(NoSuchElementException.class,()->planningRepository.findPlanningIdRequiredByFields(dishId+10,personId,restaurantId,dayOfWeek));
                assertThrows(NoSuchElementException.class,()->planningRepository.findPlanningIdRequiredByFields(dishId,personId+10,restaurantId,dayOfWeek));
                assertThrows(NoSuchElementException.class,()->planningRepository.findPlanningIdRequiredByFields(dishId,personId,restaurantId+10,dayOfWeek));
                assertThrows(NoSuchElementException.class,()->planningRepository.findPlanningIdRequiredByFields(dishId,personId,restaurantId,DayOfWeek.SUNDAY));
            }
        }

        @Nested
        class countPlanningByPersonIdAndDayOfWeek {
            @Test
            void exists() {
                //then
                assertEquals(1,planningRepository.countPlanningByPersonIdAndDayOfWeek(personId,dayOfWeek));
            }

            @Test
            void notInDB() {
                //then
                assertEquals(0,planningRepository.countPlanningByPersonIdAndDayOfWeek(personId+10,dayOfWeek));
                assertEquals(0,planningRepository.countPlanningByPersonIdAndDayOfWeek(personId,DayOfWeek.SUNDAY));
            }
        }

        @Nested
        class countPlanningByRestaurantIdAndDayOfWeek {
            @Test
            void exists() {
                //then
                assertEquals(1,planningRepository.countPlanningByRestaurantIdAndDayOfWeek(restaurantId,dayOfWeek));
            }

            @Test
            void notInDB() {
                //then
                assertEquals(0,planningRepository.countPlanningByRestaurantIdAndDayOfWeek(restaurantId+10,dayOfWeek));
                assertEquals(0,planningRepository.countPlanningByRestaurantIdAndDayOfWeek(restaurantId,DayOfWeek.SUNDAY));
            }
        }

        @Nested
        class findPersonsByRestaurantAndDayOfWeek {
            @Test
            void exists() {
                //then
                assertEquals(1,
                        planningRepository.findPersonsByRestaurantAndDayOfWeek(restaurantId, dayOfWeek).size());
            }

            @Test
            void notInDb() {
                //then
                assertEquals(0,
                        planningRepository.findPersonsByRestaurantAndDayOfWeek(restaurantId + 10, dayOfWeek).size());
                assertEquals(0,
                        planningRepository.findPersonsByRestaurantAndDayOfWeek(restaurantId, DayOfWeek.SUNDAY).size());
            }
        }

        @Nested
        class findPersonsByDishAndDayOfWeek {
            @Test
            void exists() {
                //then
                assertEquals(1,
                        planningRepository.findPersonsByDishAndDayOfWeek(dishId, dayOfWeek).size());
            }

            @Test
            void notInDb() {
                //then
                assertEquals(0,
                        planningRepository.findPersonsByDishAndDayOfWeek(dishId + 10, dayOfWeek).size());
                assertEquals(0,
                        planningRepository.findPersonsByDishAndDayOfWeek(dishId, DayOfWeek.SUNDAY).size());
            }
        }

        @Nested
        class findPersonIDsByDayOfWeek {
            @Test
            void match() {
                //then
                assertEquals(1,
                        planningRepository.findPersonIDsByDayOfWeek(dayOfWeek).size());
            }

            @Test
            void notAMatch() {
                //then
                assertEquals(0,
                        planningRepository.findPersonIDsByDayOfWeek(DayOfWeek.SUNDAY).size());
            }
        }
    }

    @Nested
    class CredentialsRepository {
        String existingUsername = "username";
        String nonExistingUsername = "nonExistingUsername";

        @Nested
        class findByUsername {
            @Test
            void exists() {
                assertTrue(credentialsRepository.findByUsername(existingUsername).isPresent());
            }

            @Test
            void notInDb() {
                assertTrue(credentialsRepository.findByUsername(nonExistingUsername).isEmpty());
            }
        }

        @Nested
        class findRequiredByUsername {
            @Test
            void exists() {
                assertDoesNotThrow(() -> credentialsRepository.findRequiredByUsername(existingUsername));
            }

            @Test
            void notInDb() {
                assertThrows(UsernameNotFoundException.class, () -> credentialsRepository.findRequiredByUsername(nonExistingUsername));
            }
        }
    }
}