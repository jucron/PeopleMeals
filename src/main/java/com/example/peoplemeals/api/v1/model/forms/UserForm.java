package com.example.peoplemeals.api.v1.model.forms;

import com.example.peoplemeals.domain.security.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class UserForm {
    private String username;
    private String password;
    private Role role;
    private String personUuid; //This is supposed to be optional
}
