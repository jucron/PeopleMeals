package com.example.peoplemeals.services;


import com.example.peoplemeals.api.v1.mapper.PersonMapper;
import com.example.peoplemeals.api.v1.mapper.PlanningMapper;
import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.api.v1.model.PlanningDTO;
import com.example.peoplemeals.api.v1.model.forms.AssociateForm;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;
import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.domain.Planning;
import com.example.peoplemeals.domain.Restaurant;
import com.example.peoplemeals.repositories.DishRepository;
import com.example.peoplemeals.repositories.PersonRepository;
import com.example.peoplemeals.repositories.PlanningRepository;
import com.example.peoplemeals.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanningServiceImpl implements PlanningService {
    private final PlanningRepository planningRepository;
    private final PlanningMapper planningMapper;
    private final DishRepository dishRepository;
    private final PersonRepository personRepository;
    private final RestaurantRepository restaurantRepository;
    private final PersonMapper personMapper;


    @Override
    public EntityDTOList<PlanningDTO> getAll() {
        return new EntityDTOList<>(planningRepository.findAll().stream()
                .map(planningMapper::planningToPlanningDTO)
                .collect(Collectors.toSet()));
    }

    @Override
    public PlanningDTO get(String planningUuid) {
        return planningMapper.planningToPlanningDTO(
                planningRepository.findRequiredByUuid(planningUuid));
    }

    @Override
    public PlanningDTO associate(AssociateForm associateForm) {
        //Validation 1 - check DayOfWeek format:
        DayOfWeek dayOfWeekCorrectFormat = validateDayOfWeek(associateForm.getDayOfWeek());
        // Validation 2 - Fetch DishId, PersonId and RestaurantId from Repo
        long dishIdFromRepo = dishRepository.findIdRequiredByUuid(associateForm.getDishUuid());
        long personIdFromRepo = personRepository.findIdRequiredByUuid(associateForm.getPersonUuid());
        long restaurantIdFromRepo = restaurantRepository.findIdRequiredByUuid(associateForm.getRestaurantUuid());
        // Validation 3 - If there is already one identical association in DB, should return error (only allow 1 meal/day/person)
        planningRepository.findPlanningIdRequiredByDishIdPersonIdRestaurantId(
                dishIdFromRepo,personIdFromRepo,restaurantIdFromRepo);
        // Validation 4 - If restaurant exceed 15 meals this day, should return error
        planningRepository.validateLessThan15RestaurantsInDayOfWeek(restaurantIdFromRepo,dayOfWeekCorrectFormat.toString());
        //Validations passed: create a new Planning with association, persist it and return a DTO of it
        Planning planningToBeSaved = new Planning()
                .withDish(new Dish().withId(dishIdFromRepo))
                .withPerson(new Person().withId(personIdFromRepo))
                .withRestaurant(new Restaurant().withId(restaurantIdFromRepo))
                .withDayOfWeek(dayOfWeekCorrectFormat);
        return planningMapper.planningToPlanningDTO(
                planningRepository.save(planningToBeSaved));
    }

    @Override
    public void disassociate(AssociateForm associateForm) {
        //Validation 1 - check DayOfWeek format:
        DayOfWeek dayOfWeekCorrectFormat = validateDayOfWeek(associateForm.getDayOfWeek());
        // Validation 2 - Fetch DishId, PersonId and RestaurantId from Repo
        long dishIdFromRepo = dishRepository.findIdRequiredByUuid(associateForm.getDishUuid());
        long personIdFromRepo = personRepository.findIdRequiredByUuid(associateForm.getPersonUuid());
        long restaurantIdFromRepo = restaurantRepository.findIdRequiredByUuid(associateForm.getRestaurantUuid());
        // Validation 3 - If there is already one identical association in DB, should return error (only allow 1 meal/day/person)

        long planningIdToRemove = planningRepository.findPlanningIdRequiredByDishIdPersonIdRestaurantId(
                dishIdFromRepo,personIdFromRepo,restaurantIdFromRepo);
        //If validations passed: proceed to the removal in DB
        planningRepository.deleteById(planningIdToRemove);
    }

    @Override
    public EntityDTOList<PersonDTO> getPersonListByRestaurantAndDay(String restaurantUuid, String dayOfWeek) {
        //Validation 1 - check DayOfWeek format:
        DayOfWeek dayOfWeekCorrectFormat = validateDayOfWeek(dayOfWeek);
        // Validation 2 - If RestaurantId is not in DB, return error
//        Restaurant restaurant = fetchRestaurantFromRepo(restaurantId);
        /**Create a collection of Persons, extracting from the PlanningRepository and filtering each request:
         * @param Restaurant must be with same restaurantId
         * @param dayOfWeek must match dayOfWeek
         * */

        Set<PersonDTO> personDTOSToBeReturned = planningRepository.findAll()
                .stream()
                .filter(planning -> planning.getDayOfWeek() == dayOfWeekCorrectFormat)
//                .filter(planning -> planning.getRestaurant().getId() == restaurantUuid)
                .map(Planning::getPerson)
                .map(personMapper::personToPersonDTO)
                .collect(Collectors.toSet());
        return new EntityDTOList<PersonDTO>().withEntityDTOList(personDTOSToBeReturned);
    }

    @Override
    public EntityDTOList<PersonDTO> getPersonListByDishAndDay(String dishUuid, String dayOfWeek) {
        //Validation 1 - check DayOfWeek format:
        DayOfWeek dayOfWeekCorrectFormat = validateDayOfWeek(dayOfWeek);
        // Validation 2 - If Dish is not in DB, return error
//        Dish dish = fetchDishFromRepo(dishId);
        /**Create a collection of Persons, extracting from the PlanningRepository and filtering each request:
         * @param Dish must be with same dishId
         * @param dayOfWeek must match dayOfWeek
         * */

        Set<PersonDTO> personDTOSToBeReturned = planningRepository.findAll()
                .stream()
                .filter(planning -> planning.getDayOfWeek() == dayOfWeekCorrectFormat)
//                .filter(planning -> planning.getDish() == dish)
                .map(Planning::getPerson)
                .map(personMapper::personToPersonDTO)
                .collect(Collectors.toSet());
        return new EntityDTOList<PersonDTO>().withEntityDTOList(personDTOSToBeReturned);
    }

    @Override
    public EntityDTOList<PersonDTO> getPersonListWithNoDishByDay(String dayOfWeek) {
        //Validation 1 - check DayOfWeek format:
        DayOfWeek dayOfWeekCorrectFormat = validateDayOfWeek(dayOfWeek);
        //Create a collection of Persons that have a planning for this DayOfWeek
        List<Person> personsInThisDayOfWeek = planningRepository.findAll()
                .stream()
                .filter(planning -> planning.getDayOfWeek() == dayOfWeekCorrectFormat)
                .map(Planning::getPerson)
                .collect(Collectors.toList());
        /**Create a collection of Persons, extracting from the PersonRepository and filtering each request:
         * @param Person must NOT be included in personListIncludedInDayOfWeek
         * */

        Set<PersonDTO> personDTOSToBeReturned = personRepository.findAll()
                .stream()
                .filter(person -> !personsInThisDayOfWeek.contains(person))
                .map(personMapper::personToPersonDTO)
                .collect(Collectors.toSet());
        return new EntityDTOList<PersonDTO>().withEntityDTOList(personDTOSToBeReturned);
    }

    private DayOfWeek validateDayOfWeek(String dayOfWeek) {
        try {
            return DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("DayOfWeek is not in valid format");
        }
    }


}



