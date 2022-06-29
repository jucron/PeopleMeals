package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.model.forms.UserForm;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;
import com.example.peoplemeals.domain.security.Credentials;

public interface CredentialsService {
    void createUser(UserForm form);

    void deactivateUser(String username);

    EntityDTOList<Credentials> getAll(Integer pageNo, Integer pageSize, String sortBy);
}
