package com.example.peoplemeals.api.v1.model.lists;

import com.example.peoplemeals.helpers.NoCoverageGenerated;
import lombok.*;

import java.util.List;

@NoCoverageGenerated
@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class EntityDTOList<T>{
    private List<T> entityDTOList;
}
