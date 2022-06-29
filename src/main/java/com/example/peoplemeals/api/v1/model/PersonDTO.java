package com.example.peoplemeals.api.v1.model;


import com.example.peoplemeals.helpers.NoCoverageGenerated;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoCoverageGenerated
@Getter
@Setter
@EqualsAndHashCode
@With
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    private UUID uuid;
    private String fullName;
    private String telephone;
    private String mobile;
    private String fiscal;

    private String creatorUsername;
    private String lastModifierUsername;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
