package com.example.peoplemeals.api.v1.model.forms;

import com.example.peoplemeals.domain.security.Role;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@With
@AllArgsConstructor
@NoArgsConstructor
public class UserForm {
    private String username;
    private String password;
    private Role role;
    private String personUuid; //This is supposed to be optional
}
