package com.example.peoplemeals.api.v1.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class DishMapper {
    private Long id;
    private String fullName;
    private String username;
    private String telephone;
    private String mobile;
    private String fiscal;
}
