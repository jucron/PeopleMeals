package com.example.peoplemeals.domain.audit;

import com.example.peoplemeals.helpers.NoCoverageGenerated;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@NoCoverageGenerated
@MappedSuperclass
@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class Auditor {

    @CreatedBy
    @Column(name = "creator_username", updatable = false)
    private String creatorUsername;


    @CreatedDate
    @Column(name = "creator_date", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "last_modifier_username")
    @LastModifiedBy
    private String lastModifierUsername;

    @Column(name = "last_modifier_date")
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
