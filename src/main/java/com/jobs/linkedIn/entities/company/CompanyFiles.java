package com.jobs.linkedIn.entities.company;

import com.jobs.linkedIn.enums.CompanyFileType;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "file_id"})
)
public class CompanyFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Company company;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CompanyFileType type;

    @Column(nullable = false, length = 10)
    private String extension;

    @Column(nullable = false)
    private Float sizeInMb;

    @Column(nullable = false)
    private String link;
}
