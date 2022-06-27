package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.model.PersonDTO;
import com.example.peoplemeals.api.v1.model.PlanningDTO;
import com.example.peoplemeals.api.v1.model.forms.AssociateForm;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;

public interface PlanningService {

    EntityDTOList<PlanningDTO> getAll();

    PlanningDTO get(String planningUuid);

    PlanningDTO associate(AssociateForm associateForm);

    void disassociate(AssociateForm associateForm);

    void remove(String planningUuid);

    EntityDTOList<PersonDTO> getPersonListByRestaurantAndDay(String restaurantUuid, String dayOfWeek);

    EntityDTOList<PersonDTO> getPersonListByDishAndDay(String dishUuid, String dayOfWeek);

    EntityDTOList<PersonDTO> getPersonListWithNoDishByDay(String dayOfWeek);
}
