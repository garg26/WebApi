package com.neostencil.model.repositories;

import com.neostencil.common.enums.PublishStatus;
import com.neostencil.model.entities.TeacherDetails;
import com.neostencil.model.entities.User;
import com.neostencil.projections.TeacherCoursesProjection;
import com.neostencil.projections.TeacherDetailedProjection;
import com.neostencil.projections.TeacherProjection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

public interface TeacherDetailsRepository
    // extends JpaRepository<TeacherDetails, Integer>, JpaSpecificationExecutor<TeacherDetails>
    extends JpaRepository<TeacherDetails, Integer>,
    JpaSpecificationExecutorWithProjection<TeacherDetails> {

  TeacherDetails findById(int id);

  TeacherDetails findBySlug(String slug);

  Page<TeacherDetails> findByStatus(PublishStatus status, Pageable pageable);

  Page<TeacherProjection> findPagedProjectedByStatusAndPositionIsGreaterThanOrderByPosition(
      PublishStatus status, Pageable pageable, int position);

  List<TeacherProjection> findAllProjectedByStatusAndPositionIsGreaterThanOrderByPosition(
      PublishStatus status, int position);

  List<TeacherProjection> findAllProjectedBy();

  @Query("select td from TeacherDetails  td where td.teacherName like %:name%")
  TeacherDetails findByTeacherName(@Param("name") String name);

  TeacherCoursesProjection findAllProjectedByUserAccount(User u);

  TeacherDetails findByUserAccount(User loggedInTeacher);
}
