package com.example.peoplemeals.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname")
    private String fullName;

    private String username;
    private String telephone;
    private String mobile;
    private String fiscal;
}
