package com.jobs.linkedIn.repositories.job;

import com.jobs.linkedIn.entities.job.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
