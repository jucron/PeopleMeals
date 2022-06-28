package com.example.peoplemeals.domain.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class Auditor {

    @Column(name = "creator_username", updatable = false)
    @CreatedBy
    private String creatorUsername;

    @Column(name = "creator_date", updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "last_modifier_username")
    @LastModifiedBy
    private String lastModifierUsername;

    @Column(name = "last_modifier_date")
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}