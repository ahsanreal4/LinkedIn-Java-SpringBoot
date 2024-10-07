package com.jobs.linkedIn.entities.company;

import jakarta.persistence.*;

@Entity
@Table
public class Company {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column
    private String about;

    @Column
    private Integer numEmployees = 0;

    @Column
    private String website;

    @Column(length = 100)
    private String headLine;
}
