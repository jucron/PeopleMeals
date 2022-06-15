package com.example.peoplemeals.api.v1.model.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class AssociateForm {
    private long personId;
    private String dishUuid;
    private long restaurantId;
    private String dayOfWeek;
}
