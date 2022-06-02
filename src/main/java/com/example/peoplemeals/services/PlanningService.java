package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.model.PersonDTOList;
import com.example.peoplemeals.api.v1.model.PlanningDTO;
import com.example.peoplemeals.api.v1.model.forms.AssociateForm;

public interface PlanningService {
    PlanningDTO associate(AssociateForm associateForm);

    PersonDTOList getPersonListByRestaurantAndDay(long restaurantId, String dayOfWeek);

    PersonDTOList getPersonListByDishAndDay(long dishId, String dayOfWeek);

    PersonDTOList getPersonListWithNoDishByDay(String dayOfWeek);
}
