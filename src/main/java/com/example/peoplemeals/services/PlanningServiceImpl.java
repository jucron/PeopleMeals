package com.example.peoplemeals.services;

/*
@Service
@RequiredArgsConstructor
public class PlanningServiceImpl implements PlanningService {
    private final PlanningRepository planningRepository;
    private final PlanningMapper planningMapper;
    private final DishRepository dishRepository;
    private final PersonRepository personRepository;
    private final RestaurantRepository restaurantRepository;
    private final PersonMapper personMapper;

    //TODO: Comments here don't add much
    @Override
    public PlanningDTO associate(AssociateForm associateForm) {
        //Validation 1 - check DayOfWeek format:
        DayOfWeek dayOfWeekCorrectFormat = validateDayOfWeek(associateForm.getDayOfWeek());

        // Validation 2 - If DishId, PersonId or RestaurantId are not in DB, return error

        Dish dishFromRepo = fetchDishFromRepo(associateForm.getPersonId());//TODO: no need to fetch entire entities
        Person personFromRepo = fetchPersonFromRepo(associateForm.getPersonId());//TODO: no need to fetch entire entities
        Restaurant restaurant = fetchRestaurantFromRepo(associateForm.getRestaurantId());//TODO: no need to fetch entire entities
        // Validation 3 - If there is already one identical association in DB, should return error (only allow 1 meal/day/person)
        checkSameAssociationsInDb(associateForm, dayOfWeekCorrectFormat, true);
        // Validation 4 - If restaurant exceed 15 meals this day, should return error
        checkIfRestaurantExceededMaximumDishes(dayOfWeekCorrectFormat, restaurant);
        //If everything passes, proceed to new association:
        // -> create a new Planning, persist it and return a DTO of it

        //TODO: Performance issues. Do you need to fetch the entire thing?
        Planning planningToBeSaved = new Planning()
                .withPerson(personFromRepo)
                .withDish(dishFromRepo)
                .withDayOfWeek(dayOfWeekCorrectFormat)
                .withRestaurant(restaurant);
        planningRepository.save(planningToBeSaved);
        return planningMapper.planningToPlanningDTO(planningToBeSaved);
    }

    @Override
    public PlanningDTO disassociate(AssociateForm associateForm) {
        //Validation 1 - check DayOfWeek format:
        DayOfWeek dayOfWeekCorrectFormat = validateDayOfWeek(associateForm.getDayOfWeek());
        // Validation 2 - If DishId, PersonId or RestaurantId are not in DB, return error
//        Dish dishFromRepo = fetchDishFromRepo(associateForm.getDishUuid()); //todo: solve this
        Person personFromRepo = fetchPersonFromRepo(associateForm.getPersonId());
        Restaurant restaurant = fetchRestaurantFromRepo(associateForm.getRestaurantId());
        // Validation 3 - If there is already one identical association in DB, should return error (only allow 1 meal/day/person)
        Planning planningToRemove = checkSameAssociationsInDb(associateForm, dayOfWeekCorrectFormat, false);
        //If validations passed: proceed to the removal in DB
        planningRepository.delete(planningToRemove);
        planningToRemove.setId(null); //returning a planningDTO without ID
        return planningMapper.planningToPlanningDTO(planningToRemove);
    }

    private Restaurant fetchRestaurantFromRepo(long restaurantId) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        //If no Restaurant entities exists in DB, should return error
        if (restaurantOptional.isEmpty()) {
            throw new NoSuchElementException("No Restaurant with this ID was found in Database");
        }
        return restaurantOptional.get();
    }

    private Person fetchPersonFromRepo(long personId) {
        Optional<Person> personOptional = personRepository.findById(personId);
        //If no Person entities exists in DB, should return error
        if (personOptional.isEmpty()) {
            throw new NoSuchElementException("No Person with this ID was found in Database");
        }
        return personOptional.get();
    }

    private Dish fetchDishFromRepo(long dishId) {
        Optional<Dish> dishOptional = dishRepository.findById(dishId);
        //If no Person entities exists in DB, should return error
        if (dishOptional.isEmpty()) {
            throw new NoSuchElementException("No Dish with this ID was found in Database");
        }
        return dishOptional.get();

    }

    private Planning checkSameAssociationsInDb(AssociateForm associateForm, DayOfWeek dayOfWeekCorrectFormat, boolean isAssociation) {
        //Fetch a List of Plannings in DB that matches the associateForm given
        List<Planning> planningListWithSameData = planningRepository.findAll().stream()
                .filter(planning -> Objects.equals(planning.getDish().getUuid().toString(), associateForm.getDishUuid()))
                .filter(planning -> planning.getPerson().getId() == associateForm.getPersonId())
                .filter(planning -> planning.getRestaurant().getId() == associateForm.getRestaurantId())
                .filter(planning -> planning.getDayOfWeek() == dayOfWeekCorrectFormat)
                .collect(Collectors.toList());
        if (isAssociation) { //For an Association: If there is a match, we can't proceed with association
            if (planningListWithSameData.size() > 0) {
                throw new IllegalArgumentException("This planning already exists in Database");
            }
        } else { //Disassociation: To remove an association we expect ONE Planning matching the Form
            if (planningListWithSameData.size() != 1) {
                throw new NoSuchElementException("Cannot update a plan that does not exists in Database");
            }
            return planningListWithSameData.get(0); //In disassociation, we need this Planning
        }
        return null;
    }

    private void checkIfRestaurantExceededMaximumDishes(DayOfWeek dayOfWeekCorrectFormat, Restaurant restaurant) {
        //Fetch list of plannings that have same Restaurant and DayOfWeek
        List<Planning> planningListInThisDayAndRestaurant = planningRepository.findAll()
                .stream()
                .filter(planning -> planning.getDayOfWeek() == dayOfWeekCorrectFormat)
                .filter(planning -> planning.getRestaurant() == restaurant)
                .collect(Collectors.toList());
        //If there are at least 15 plannings for this day and Restaurant, it has achieved maximum associations
        if (planningListInThisDayAndRestaurant.size() >= 15) {
            throw new IllegalArgumentException("This restaurant have already the maximum number of dishes");
        }
    }

    @Override
    public EntityDTOList<PersonDTO> getPersonListByRestaurantAndDay(long restaurantId, String dayOfWeek) {
        //Validation 1 - check DayOfWeek format:
        DayOfWeek dayOfWeekCorrectFormat = validateDayOfWeek(dayOfWeek);
        // Validation 2 - If RestaurantId is not in DB, return error
        Restaurant restaurant = fetchRestaurantFromRepo(restaurantId);
        /**Create a collection of Persons, extracting from the PlanningRepository and filtering each request:
         * @param Restaurant must be with same restaurantId
         * @param dayOfWeek must match dayOfWeek
         * */
/*
        Set<PersonDTO> personDTOSToBeReturned = planningRepository.findAll()
                .stream()
                .filter(planning -> planning.getDayOfWeek() == dayOfWeekCorrectFormat)
                .filter(planning -> planning.getRestaurant().getId() == restaurantId)
                .map(Planning::getPerson)
                .map(personMapper::personToPersonDTO)
                .collect(Collectors.toSet());
        return new EntityDTOList<PersonDTO>().withEntityDTOList(personDTOSToBeReturned);
    }

    @Override
    public EntityDTOList<PersonDTO> getPersonListByDishAndDay(long dishId, String dayOfWeek) {
        //Validation 1 - check DayOfWeek format:
        DayOfWeek dayOfWeekCorrectFormat = validateDayOfWeek(dayOfWeek);
        // Validation 2 - If Dish is not in DB, return error
        Dish dish = fetchDishFromRepo(dishId);
        /**Create a collection of Persons, extracting from the PlanningRepository and filtering each request:
         * @param Dish must be with same dishId
         * @param dayOfWeek must match dayOfWeek
         * */
/*
        Set<PersonDTO> personDTOSToBeReturned = planningRepository.findAll()
                .stream()
                .filter(planning -> planning.getDayOfWeek() == dayOfWeekCorrectFormat)
                .filter(planning -> planning.getDish() == dish)
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
/*
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

 */

