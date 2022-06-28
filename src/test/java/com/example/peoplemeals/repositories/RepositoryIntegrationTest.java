package com.example.peoplemeals.repositories;

import com.example.peoplemeals.config.PersistenceConfig;
import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.domain.Planning;
import com.example.peoplemeals.domain.Restaurant;
import com.example.peoplemeals.domain.security.Credentials;
import com.example.peoplemeals.domain.security.Role;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.NestedRuntimeException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import testUtils.PojoExampleCreation;

import javax.validation.ConstraintViolationException;
import java.time.DayOfWeek;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(PersistenceConfig.class)
@WithMockUser(username = "username_example")
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

    @Nested
    class RestaurantRepository {
        long existingRestaurantId = planningToBeTested.getRestaurant().getId();
        long nonExistingRestaurantId = existingRestaurantId + 50;

        @Nested
        class findMaxNumberOfMealsPerDayByRestaurantId {
            @Test
            void exists() {
                assertTrue(restaurantRepository.findMaxNumberOfMealsPerDayByRestaurantId(existingRestaurantId).isPresent());
            }

            @Test
            void notInDb() {
                assertTrue(restaurantRepository.findMaxNumberOfMealsPerDayByRestaurantId(nonExistingRestaurantId).isEmpty());
            }
        }

        @Nested
        class findMaxNumberOfMealsPerDayRequiredByRestaurantId {
            @Test
            void exists() {
                assertDoesNotThrow(() -> restaurantRepository.findMaxNumberOfMealsPerDayRequiredByRestaurantId(existingRestaurantId));
            }

            @Test
            void notInDb() {
                assertThrows(NoSuchElementException.class, () -> restaurantRepository.findMaxNumberOfMealsPerDayRequiredByRestaurantId(nonExistingRestaurantId));
            }
        }
    }

    @Nested
    class EntityFieldsValidations {
        @Nested
        class DishEntity {
            @Test
            void blankOrNull() {
                Dish dishWithNullName = new Dish().withName(null);
                Dish dishWithEmptyName = new Dish().withName("");
                assertThrows(ConstraintViolationException.class, () -> dishRepository.save(dishWithNullName));
                assertThrows(ConstraintViolationException.class, () -> dishRepository.save(dishWithEmptyName));
            }
        }

        @Nested
        class PersonEntity {
            @Test
            void blankOrNull() {
                Person personWithNullName = new Person().withFullName(null);
                Person personWithBlankName = new Person().withFullName("");
                assertThrows(ConstraintViolationException.class, () -> personRepository.save(personWithNullName));
                assertThrows(ConstraintViolationException.class, () -> personRepository.save(personWithBlankName));
            }
        }

        @Nested
        class RestaurantEntity {
            @Test
            void blankOrNull() {
                Restaurant restaurantWithNullName = new Restaurant().withName(null);
                Restaurant restaurantWithBlankName = new Restaurant().withName("");
                assertThrows(ConstraintViolationException.class, () -> restaurantRepository.save(restaurantWithNullName));
                assertThrows(ConstraintViolationException.class, () -> restaurantRepository.save(restaurantWithBlankName));
            }
        }

        @Nested
        class PlanningEntity {
            @Test
            void blankOrNull() {
                Planning planningWithNullDayOfWeek = new Planning().withDayOfWeek(null);
                assertThrows(ConstraintViolationException.class, () -> planningRepository.save(planningWithNullDayOfWeek));
            }
        }

        @Nested
        class CredentialsEntity {
            @Test
            void blankOrNull() {
                Credentials credentialsCorrect = new Credentials()
                        .withUsername("username").withPassword("pass").withRole(Role.USER);
                Credentials credentialsWithNullUsername = credentialsCorrect.withUsername(null);
                Credentials credentialsWithNullPassword = credentialsCorrect.withPassword(null);
                Credentials credentialsWithBlankUsername = credentialsCorrect.withUsername("");
                Credentials credentialsWithBlankPassword = credentialsCorrect.withPassword("");
                Credentials credentialsWithNullRole = credentialsCorrect.withRole(null);

                assertThrows(ConstraintViolationException.class, () -> credentialsRepository.save(credentialsWithNullUsername));
                assertThrows(ConstraintViolationException.class, () -> credentialsRepository.save(credentialsWithNullPassword));
                assertThrows(ConstraintViolationException.class, () -> credentialsRepository.save(credentialsWithBlankUsername));
                assertThrows(ConstraintViolationException.class, () -> credentialsRepository.save(credentialsWithBlankPassword));
                assertThrows(ConstraintViolationException.class, () -> credentialsRepository.save(credentialsWithNullRole));

            }
        }
    }

    @Nested
    class EntityAuditFields {
        @Nested
        class auditFieldsInPerson {
            @Test
            void creatingEntity() {
                Person person = personRepository.save(PojoExampleCreation.createPersonExample(50));

                assertNotNull(person.getCreatedDate());
                assertNotNull(person.getCreatorUsername());
                assertNotNull(person.getLastModifiedDate());
                assertNotNull(person.getLastModifierUsername());
                //todo: bug in Audit spotted while updating fields
            }
        }

        @Nested
        class auditFieldsInRestaurant {
            @Test
            void creatingEntity() {
                Restaurant restaurant = restaurantRepository.save(PojoExampleCreation.createRestaurantExample(50)
                        .withDishes(new HashSet<>(List.of(dishToBeTested))));

                assertNotNull(restaurant.getCreatedDate());
                assertNotNull(restaurant.getCreatorUsername());
                assertNotNull(restaurant.getLastModifiedDate());
                assertNotNull(restaurant.getLastModifierUsername());
                //todo: bug in Audit spotted while updating fields
            }
        }
    }
}