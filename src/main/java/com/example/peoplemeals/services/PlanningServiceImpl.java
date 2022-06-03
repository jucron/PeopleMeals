package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.mapper.PersonMapper;
import com.example.peoplemeals.api.v1.mapper.PlanningMapper;
import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.api.v1.model.PersonDTOList;
import com.example.peoplemeals.api.v1.model.PlanningDTO;
import com.example.peoplemeals.api.v1.model.forms.AssociateForm;
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

import java.util.*;
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

    //todo: For all streams on this Class, should replace them with specific Repo's Queries!

    @Override
    public PlanningDTO associate(AssociateForm associateForm) {
        //Creating a List of Plannings in DB that matches the associateForm given
        List<Planning> filteredPlanningList = planningRepository.findAll()
                .stream()
                .filter(planning -> planning.getDish().getId()==associateForm.getDishId())
                .filter(planning -> planning.getPerson().getId()==associateForm.getPersonId())
                .filter(planning -> planning.getDayOfWeek()==associateForm.getDayOfWeek())
                .collect(Collectors.toList());
        /**@First_option: task is to remove association*/
        if (associateForm.isRemove()) { //this parameter validates the type of process
            //To remove an association we expect ONE Planning matching the Form, otherwise should return error
            if (filteredPlanningList.size() != 1) {
                throw new NoSuchElementException("Cannot update a plan that does not exists in Database");
            }
            //If one Planning was found with info given, proceed to the removal in DB
            Planning planningToRemove = filteredPlanningList.get(0);
            planningRepository.delete(planningToRemove);
            planningToRemove.setId(null); //returning a planningDTO without ID
            return planningMapper.planningToPlanningDTO(planningToRemove);
         /**@Second_option: task is to associate person & dish & dayOfWeek */
        } else {
            //If there is already one identical association in DB, should return error
            if (filteredPlanningList.size() > 0) {
                throw new IllegalArgumentException("This planning already exists in Database");
            }
            Optional<Dish> dishOptional = dishRepository.findById(associateForm.getDishId());
            Optional<Person> personOptional = personRepository.findById(associateForm.getPersonId());
            //If no Dish or Person entities exists in DB, or DayOfWeek is null, should return error
            if (dishOptional.isEmpty() || personOptional.isEmpty() || associateForm.getDayOfWeek()==null) {
                throw new NoSuchElementException("No Person or Dish was found in Database, or Day is null.");
            }
            //Every validation passed: create a new Planning, persist it and return a DTO of it
            Planning planningToBeSaved = new Planning()
                    .withPerson(personOptional.get())
                    .withDish(dishOptional.get())
                    .withDayOfWeek(associateForm.getDayOfWeek());
            planningRepository.save(planningToBeSaved);
            return planningMapper.planningToPlanningDTO(planningToBeSaved);
        }
    }

    @Override
    public PersonDTOList getPersonListByRestaurantAndDay(long restaurantId, String dayOfWeek) {
        //Fetch restaurant by ID
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (restaurantOptional.isEmpty()) { //If restaurant not found in DB, should return error
            throw new NoSuchElementException("No Restaurant with this ID was found in Database");
        }
        //Get list of Dishes belonging to this restaurant
        Set<Dish> dishesFromThisRestaurant = restaurantOptional.get().getDishes();
        /**Create a collections of Persons, extracting from the PlanningRepository and filtering each request:
         * @param Dish must be part of Restaurant's Dishes
         * @param dayOfWeek must match dayOfWeek
         * */
        Set<PersonDTO> personDTOSToBeReturned = planningRepository.findAll()
                .stream()
                .filter(planning -> Objects.equals(planning.getDayOfWeek().toString(), dayOfWeek))
                .filter(planning -> dishesFromThisRestaurant.contains(planning.getDish()))
                .map(Planning::getPerson)
                .map(personMapper::personToPersonDTO)
                .collect(Collectors.toSet());
        System.out.println(personDTOSToBeReturned);
        return new PersonDTOList().withPersonDTOList(personDTOSToBeReturned);
    }

    @Override
    public PersonDTOList getPersonListByDishAndDay(long dishId, String dayOfWeek) {
        //Fetch dish by ID
        Optional<Dish> dishOptional = dishRepository.findById(dishId);
        if (dishOptional.isEmpty()) { //If dish not found in DB, should return error
            throw new NoSuchElementException("No Dish with this ID was found in Database");
        }
        /**Create a collections of Persons, extracting from the PlanningRepository and filtering each request:
         * @param Dish must match dishId
         * @param dayOfWeek must match dayOfWeek
         * */
        Set<PersonDTO> personDTOSToBeReturned = planningRepository.findAll()
                        .stream()
                        .filter(planning -> Objects.equals(planning.getDayOfWeek().toString(), dayOfWeek))
                        .filter(planning -> planning.getDish().getId()==dishId)
                        .map(Planning::getPerson)
                        .map(personMapper::personToPersonDTO)
                        .collect(Collectors.toSet());
        return new PersonDTOList().withPersonDTOList(personDTOSToBeReturned);
    }

    @Override
    public PersonDTOList getPersonListWithNoDishByDay(String dayOfWeek) {
        //Create a collection of Persons that have a planning for this DayOfWeek
        List<Person> personListIncludedInDayOfWeek = planningRepository.findAll()
                .stream()
                .filter(planning -> Objects.equals(planning.getDayOfWeek().toString(), dayOfWeek))
                .map(Planning::getPerson)
                .collect(Collectors.toList());

        if (personListIncludedInDayOfWeek.isEmpty()) { //If no Planning of this DayOfWeek is found in DB, should return error
            throw new NoSuchElementException("No Persons were found in Database");
        }
        /**Create a collections of Persons, extracting from the PersonRepository and filtering each request:
         * @param Person must NOT be included in personListIncludedInDayOfWeek
         * */
        Set<PersonDTO> personDTOSToBeReturned = personRepository.findAll()
                .stream()
                .filter(person -> !personListIncludedInDayOfWeek.contains(person))
                .map(personMapper::personToPersonDTO)
                .collect(Collectors.toSet());
        return new PersonDTOList().withPersonDTOList(personDTOSToBeReturned);
    }
}
