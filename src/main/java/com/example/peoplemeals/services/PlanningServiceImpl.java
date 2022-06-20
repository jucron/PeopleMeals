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
import java.util.stream.Collectors;

import static com.example.peoplemeals.helpers.DayOfWeekHelper.validateDayOfWeek;

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
        DayOfWeek dayOfWeekCorrectFormat = validateDayOfWeek(associateForm.getDayOfWeek());
        long dishIdFromRepo = dishRepository.findIdRequiredByUuid(associateForm.getDishUuid());
        long personIdFromRepo = personRepository.findIdRequiredByUuid(associateForm.getPersonUuid());
        long restaurantIdFromRepo = restaurantRepository.findIdRequiredByUuid(associateForm.getRestaurantUuid());
        planningRepository.validateNoPlanForThisPersonInDayOfWeek(personIdFromRepo, dayOfWeekCorrectFormat);
        planningRepository.validateLessThan15RestaurantsInDayOfWeek(restaurantIdFromRepo, dayOfWeekCorrectFormat);
        //Validations passed: create a new Planning with association, persist it and return a DTO of it
        return planningMapper.planningToPlanningDTO(
                planningRepository.save(new Planning()
                        .withDish(new Dish().withId(dishIdFromRepo))
                        .withPerson(new Person().withId(personIdFromRepo))
                        .withRestaurant(new Restaurant().withId(restaurantIdFromRepo))
                        .withDayOfWeek(dayOfWeekCorrectFormat)));
    }

    @Override
    public void disassociate(AssociateForm associateForm) {
        DayOfWeek dayOfWeekCorrectFormat = validateDayOfWeek(associateForm.getDayOfWeek());
        long dishIdFromRepo = dishRepository.findIdRequiredByUuid(associateForm.getDishUuid());
        long personIdFromRepo = personRepository.findIdRequiredByUuid(associateForm.getPersonUuid());
        long restaurantIdFromRepo = restaurantRepository.findIdRequiredByUuid(associateForm.getRestaurantUuid());
        long planningIdToRemove = planningRepository.findPlanningIdRequiredByFields(
                dishIdFromRepo,personIdFromRepo,restaurantIdFromRepo,dayOfWeekCorrectFormat);
        //If validations passed: proceed to the removal in DB
        planningRepository.deleteById(planningIdToRemove);
    }

    @Override
    public EntityDTOList<PersonDTO> getPersonListByRestaurantAndDay(String restaurantUuid, String dayOfWeek) {
        DayOfWeek dayOfWeekCorrectFormat = validateDayOfWeek(dayOfWeek);
        long restaurantIdFromRepo = restaurantRepository.findIdRequiredByUuid(restaurantUuid);
        return new EntityDTOList<PersonDTO>()
                .withEntityDTOList(planningRepository
                        .findPersonsByRestaurantAndDayOfWeek(restaurantIdFromRepo, dayOfWeekCorrectFormat)
                        .stream()
                        .map(personMapper::personToPersonDTO)
                        .collect(Collectors.toSet()));
    }

    @Override
    public EntityDTOList<PersonDTO> getPersonListByDishAndDay(String dishUuid, String dayOfWeek) {
        DayOfWeek dayOfWeekCorrectFormat = validateDayOfWeek(dayOfWeek);
        long dishIdFromRepo = dishRepository.findIdRequiredByUuid(dishUuid);
        return new EntityDTOList<PersonDTO>()
                .withEntityDTOList(planningRepository
                        .findPersonsByDishAndDayOfWeek(dishIdFromRepo, dayOfWeekCorrectFormat)
                        .stream()
                        .map(personMapper::personToPersonDTO)
                        .collect(Collectors.toSet()));
    }

    @Override
    public EntityDTOList<PersonDTO> getPersonListWithNoDishByDay(String dayOfWeek) {
        DayOfWeek dayOfWeekCorrectFormat = validateDayOfWeek(dayOfWeek);
        List<Long> personsIDsInThisDayOfWeek = planningRepository.findPersonIDsByDayOfWeek(dayOfWeekCorrectFormat);
        return new EntityDTOList<PersonDTO>()
                .withEntityDTOList(personRepository
                        .findAllNotInList(personsIDsInThisDayOfWeek)
                        .stream()
                        .map(personMapper::personToPersonDTO)
                        .collect(Collectors.toSet()));
    }
}



