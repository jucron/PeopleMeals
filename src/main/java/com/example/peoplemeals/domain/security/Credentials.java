package com.example.peoplemeals.domain.security;

import com.example.peoplemeals.domain.Person;
import com.example.peoplemeals.helpers.NoCoverageGenerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoCoverageGenerated
@Entity
@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class Credentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Column(name = "deactivation_date")
    private LocalDateTime deactivationDate; //If null, User still active

    @NotNull
//    @Enumerated(EnumType.STRING)
    private String role;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;


}
