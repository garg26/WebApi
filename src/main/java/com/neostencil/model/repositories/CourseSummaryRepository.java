package com.neostencil.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neostencil.model.entities.CourseSummary;

public interface CourseSummaryRepository extends JpaRepository<CourseSummary, Integer> {

  CourseSummary findByCourseSummaryId(int courseSummaryId);
}
