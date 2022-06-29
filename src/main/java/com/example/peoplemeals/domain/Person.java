package com.example.peoplemeals.domain;

import com.example.peoplemeals.domain.audit.Auditor;
import com.example.peoplemeals.helpers.NoCoverageGenerated;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@NoCoverageGenerated
@Entity
@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Person extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    @NotBlank
    @Column(name = "fullname")
    private String fullName;

    private String telephone;
    private String mobile;
    private String fiscal;
}
