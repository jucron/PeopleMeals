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
    private long dishId;
    private String dayOfWeek;
    private boolean remove; //true if the operation is to disassociate between person and dish
}
