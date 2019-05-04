package com.neostencil.model.repositories;

import com.neostencil.model.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Integer> {

  Test findById(int id);
}
