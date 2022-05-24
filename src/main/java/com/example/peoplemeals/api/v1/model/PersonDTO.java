package com.example.peoplemeals.api.v1.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    private Long id;
    private String fullName;
    private String username;
    private String telephone;
    private String mobile;
    private String fiscal;
}
