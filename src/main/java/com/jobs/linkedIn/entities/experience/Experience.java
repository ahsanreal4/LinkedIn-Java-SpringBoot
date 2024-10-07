package com.jobs.linkedIn.entities.experience;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jobs.linkedIn.entities.company.Company;
import com.jobs.linkedIn.entities.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table
public class Experience {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Company company;

    @Column(nullable = false, length = 100)
    private String position;

    @Column(nullable = false)
    private Float experienceInYears;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column
    private LocalDate endDate;
}
