package com.neostencil.model.repositories;

import com.neostencil.model.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {

  Quiz findByQuizId(int id);

}
