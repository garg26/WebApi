package com.neostencil.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.neostencil.common.enums.AnswerSheetCategory;
import com.neostencil.model.entities.AnswerSheet;

public interface AnswerSheetRepository extends JpaRepository<AnswerSheet, Integer> {

  List<AnswerSheet> findByCategory(AnswerSheetCategory category);

}
