package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.api.v1.model.PlanningDTO;
import com.example.peoplemeals.api.v1.model.forms.AssociateForm;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;

public interface PlanningService {
    PlanningDTO associate(AssociateForm associateForm);

    PlanningDTO disassociate(AssociateForm associateForm);

    EntityDTOList<PersonDTO> getPersonListByRestaurantAndDay(long restaurantId, String dayOfWeek);

    EntityDTOList<PersonDTO> getPersonListByDishAndDay(long dishId, String dayOfWeek);

    EntityDTOList<PersonDTO> getPersonListWithNoDishByDay(String dayOfWeek);
}
