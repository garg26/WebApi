package com.neostencil.model.repositories;

import com.neostencil.model.entities.CustomStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomStudentRepository extends JpaRepository<CustomStudent, Integer> {

}
