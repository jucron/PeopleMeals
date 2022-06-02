package com.example.peoplemeals.services;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanningServiceImpl implements PlanningService {
    private final PlanningRepository planningRepository;
    private final PlanningMapper planningMapper;
    private final DishRepository dishRepository;
    private final PersonRepository personRepository;

    @Override
    public PlanningDTO associate(AssociateForm associateForm) {
        try {
            //Creating a List of Plannings in DB that matches the associateForm given
            List<Planning> associatedPlannings = planningRepository.findAll()
                    .stream()
                    .filter(planning -> planning.getDish().getId()==associateForm.getDishId())
                    .filter(planning -> planning.getPerson().getId()==associateForm.getPersonId())
                    .filter(planning -> planning.getDayOfWeek()==associateForm.getDayOfWeek())
                    .collect(Collectors.toList());

            if (associateForm.isRemove()) { //First option: task is to remove association
                //Expected ONE Planning matching the Form, otherwise should return error
                if (associatedPlannings.size() != 1) {
                    throw new IllegalArgumentException("Cannot update a plan that does not exists in Database");
                }
                Planning planningToRemove = associatedPlannings.get(0);
                planningRepository.delete(planningToRemove);
                planningToRemove.setId(null); //returning a planning without ID
                return planningMapper.planningToPlanningDTO(planningToRemove);
            } else { //Second option: task is to associate person, dish and dayOfWeek
                //If there is an identical association in DB, should return error
                if (associatedPlannings.size() > 0) {
                    throw new IllegalArgumentException("This planning already exists in Database");
                }
                Optional<Dish> dishOptional = dishRepository.findById(associateForm.getDishId());
                Optional<Person> personOptional = personRepository.findById(associateForm.getPersonId());
                //If no Dish, Person entities exists or DayOfWeek is null, should return error
                if (dishOptional.isEmpty() || personOptional.isEmpty() || associateForm.getDayOfWeek()==null) {
                    throw new IllegalArgumentException("No Person or Dish was found in Database. Or Day is null.");
                }
                Planning planningToBeSaved = new Planning()
                        .withPerson(personOptional.get())
                        .withDish(dishOptional.get())
                        .withDayOfWeek(associateForm.getDayOfWeek());
                planningRepository.save(planningToBeSaved);
                return planningMapper.planningToPlanningDTO(planningToBeSaved);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            //todo: How to handle this?
        }
        return null;
    }

    @Override
    public PersonDTOList getPersonListByRestaurantAndDay(long restaurantId, String dayOfWeek) {
        return null;
    }

    @Override
    public PersonDTOList getPersonListByDishAndDay(long dishId, String dayOfWeek) {
        return null;
    }

    @Override
    public PersonDTOList getPersonListWithNoDishByDay(String dayOfWeek) {
        return null;
    }
}
