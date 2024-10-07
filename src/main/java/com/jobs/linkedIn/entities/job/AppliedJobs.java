package com.jobs.linkedIn.entities.job;

import com.jobs.linkedIn.entities.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "job_id"})
)
public class AppliedJobs {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Job job;

    @Column(nullable = false)
    private Float fileSizeInMb;

    @Column(nullable = false)
    private String fileLink;

    @Column(nullable = false)
    private Date appliedDate;
}
