package com.example.peoplemeals.api.v1.model.lists;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.Set;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class EntityDTOList<T>{
    private Set<T> entityDTOList;
}
