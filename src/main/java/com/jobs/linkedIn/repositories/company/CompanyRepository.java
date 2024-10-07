package com.jobs.linkedIn.repositories.company;

import com.jobs.linkedIn.entities.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
