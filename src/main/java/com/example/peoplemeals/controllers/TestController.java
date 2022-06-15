package com.example.peoplemeals.controllers;

/*
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    private final long DISH_ID = 1;
    private final long PERSON_ID = 1;
    private final long RESTAURANT_ID = 1;

    private final DishRepository dishRepository;
    private final PlanningRepository planningRepository;
    private final PersonRepository personRepository;
    private final RestaurantRepository restaurantRepository;


    @GetMapping({"/v1"})
    public long testPerformance() {
        List<Planning> planningListWithSameData = planningRepository.findAll().stream()
                .filter(planning -> planning.getDish().getId() == DISH_ID)
                .filter(planning -> planning.getPerson().getId() == PERSON_ID)
                .filter(planning -> planning.getRestaurant().getId() == RESTAURANT_ID)
                .collect(Collectors.toList());

        return planningListWithSameData.size();
    }

    @GetMapping({"/v2"})
    public long testPerformanceV2() {
        int planningListWithSameData = planningRepository.findPlanningByDishIdAndPersonId(DISH_ID, PERSON_ID, RESTAURANT_ID);
        return planningListWithSameData;
    }

    @PostConstruct
    @Transactional
    public void populate() {
        System.out.println("populating");


        Set<Dish> dishes = Set.of(new Dish(1L, 1 + "", ""));
        dishRepository.saveAll(dishes);
        Restaurant restaurant = new Restaurant(1L, 1 + "", null, null, DayOfWeek.MONDAY, dishes);
        restaurantRepository.save(restaurant);


        Person person = new Person(1L, "i", "", null, null, null);
        personRepository.save(person);


        Set<Planning> plannings = new HashSet<>();
        for (long i = 0; i < 1000000; i++) {
            plannings.add(new Planning(i + 1, DayOfWeek.MONDAY, dishes.stream().findAny().get(), person, restaurant));
        }
        planningRepository.saveAll(plannings);

        System.out.println("populated");
    }
}

 */
