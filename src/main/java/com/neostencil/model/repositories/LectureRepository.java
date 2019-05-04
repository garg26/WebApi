
package com.neostencil.model.repositories;

import com.neostencil.model.entities.Lecture;
import com.neostencil.projections.LectureProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {

  Lecture findById(int id);

  LectureProjection findAllProjectedById(int id);

  List<LectureProjection> findAllProjectedBy();
}
