package com.neostencil.model.repositories;

import com.neostencil.model.entities.QuizTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizTemplateRepository extends JpaRepository<QuizTemplate, Integer> {

  QuizTemplate findByQuizQuizId(int quizId);

  QuizTemplate findQuizTemplateBySlug(String slug);

  QuizTemplate findQuizTemplateById(int id);

}
