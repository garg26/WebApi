package com.neostencil.model.repositories;

import com.neostencil.model.entities.UserExamSegment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserExamSegmentRepository extends JpaRepository<UserExamSegment, Integer> {

    UserExamSegment findById(int id);

}
