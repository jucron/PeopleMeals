package com.example.peoplemeals.api.v1.model.forms;

import com.example.peoplemeals.helpers.NoCoverageGenerated;
import lombok.*;

@NoCoverageGenerated
@Getter
@Setter
@EqualsAndHashCode
@With
@AllArgsConstructor
@NoArgsConstructor
public class UserForm {
    private String username;
    private String password;
    private String role;
    private String personUuid; //This is supposed to be optional
}
