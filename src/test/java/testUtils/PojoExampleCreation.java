package testUtils;

import com.example.peoplemeals.api.v1.model.DishDTO;
import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.api.v1.model.PlanningDTO;
import com.example.peoplemeals.api.v1.model.RestaurantDTO;
import com.example.peoplemeals.domain.Dish;
import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.domain.Planning;
import com.example.peoplemeals.domain.Restaurant;
import com.example.peoplemeals.helpers.NoCoverageGenerated;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@NoCoverageGenerated
public class PojoExampleCreation {

    public static Dish createDishExample (int ref) {
        return new Dish()
                .withId(1L+ref)
                .withUuid(UUID.randomUUID())
                .withName("dishName_example_"+ref)
                .withRecipeUrl("url_example_"+ref);
    }

    public static Person createPersonExample (int ref) {
        return new Person()
                .withId(10L+ref)
                .withUuid(UUID.randomUUID())
                .withFullName("fullName_example_"+ref)
                .withMobile("mobile_example_"+ref)
                .withTelephone("telephone_example_"+ref)
                .withFiscal("fiscal_example_"+ref);
    }

    public static Planning createPlanningExample (int ref) {
        return new Planning()
                .withId(50L+ref)
                .withUuid(UUID.randomUUID())
                .withDayOfWeek(DayOfWeek.MONDAY)
                .withDish(createDishExample(ref))
                .withPerson(createPersonExample(ref))
                .withRestaurant(createRestaurantExample(ref));
    }

    public static Restaurant createRestaurantExample (int ref) {
        return new Restaurant()
                .withId(100L + ref)
                .withUuid(UUID.randomUUID())
                .withName("restaurantName_example_" + ref)
                .withOpeningTime(LocalTime.of(8, 0))
                .withClosingTime(LocalTime.of(20, 0))
                .withStaffRestDay(DayOfWeek.SUNDAY)
                .withDishes(Set.of(createDishExample(ref)))
                .withMaxNumberOfMealsPerDay(10);
    }

    public static DishDTO createDishDTOExample (int ref) {
        return new DishDTO()
                .withUuid(UUID.randomUUID())
                .withName("dishName_example_"+ref)
                .withRecipeUrl("url_example_"+ref);
    }

    public static PersonDTO createPersonDTOExample (int ref) {
        return new PersonDTO()
                .withUuid(UUID.randomUUID())
                .withFullName("fullName_example_"+ref)
                .withMobile("mobile_example_"+ref)
                .withTelephone("telephone_example_"+ref)
                .withFiscal("fiscal_example_"+ref);
    }

    public static PlanningDTO createPlanningDTOExample (int ref) {
        return new PlanningDTO()
                .withUuid(UUID.randomUUID())
                .withDayOfWeek(DayOfWeek.MONDAY)
                .withDishDTO(createDishDTOExample(ref))
                .withPersonDTO(createPersonDTOExample(ref));
    }

    public static RestaurantDTO createRestaurantDTOExample (int ref) {
        return new RestaurantDTO()
                .withUuid(UUID.randomUUID())
                .withName("restaurantName_example_" + ref)
                .withOpeningTime(LocalTime.of(8, 0))
                .withClosingTime(LocalTime.of(20, 20))
                .withStaffRestDay(DayOfWeek.SUNDAY)
                .withDishDTOS((Set.of(createDishDTOExample(ref))))
                .withMaxNumberOfMealsPerDay(10);
    }
}
