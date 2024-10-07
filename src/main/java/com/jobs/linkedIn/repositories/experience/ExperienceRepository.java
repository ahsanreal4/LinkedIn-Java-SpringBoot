package com.jobs.linkedIn.repositories.experience;

import com.jobs.linkedIn.entities.experience.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
}
