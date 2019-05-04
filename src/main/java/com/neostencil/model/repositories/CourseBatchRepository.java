package com.neostencil.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.model.entities.CourseBatch;
import com.neostencil.projections.CourseBatchProjection;

public interface CourseBatchRepository extends JpaRepository<CourseBatch, Integer> {

  CourseBatch findById(int id);

  CourseBatch findBatchNameById(int id);

  @Query("select cb from CourseBatch cb where cb.course.id in :courseIds order by cb.course.id")
  List<CourseBatch> fetchCourseBatchNames(@Param("courseIds") List<Integer> courseIds);

  List<CourseBatch> findByIdIn(List<Integer> courseBatchIds);

  List<CourseBatch> findByStatus(PublishStatus status);

  CourseBatchProjection findAllProjectedById(int id);
}
