package com.jobs.linkedIn.repositories.job;

import com.jobs.linkedIn.entities.job.AppliedJobs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppliedJobsRepository extends JpaRepository<AppliedJobs, Long> {
}
