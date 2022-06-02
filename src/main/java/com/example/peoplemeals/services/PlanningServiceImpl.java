package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.model.PersonDTOList;
import com.example.peoplemeals.api.v1.model.PlanningDTO;
import com.example.peoplemeals.api.v1.model.forms.AssociateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanningServiceImpl implements PlanningService {
    @Override
    public PlanningDTO associate(AssociateForm associateForm) {
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
