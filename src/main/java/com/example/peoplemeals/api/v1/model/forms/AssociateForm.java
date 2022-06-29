package com.example.peoplemeals.api.v1.model.forms;

import com.example.peoplemeals.helpers.NoCoverageGenerated;
import lombok.*;

@NoCoverageGenerated
@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class AssociateForm {
    private String personUuid;
    private String dishUuid;
    private String restaurantUuid;
    private String dayOfWeek;
}
