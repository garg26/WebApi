package com.neostencil.model.repositories;

import com.neostencil.common.enums.ExamSegmentTypes;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.model.entities.AllCustomCourseWithBatch;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import com.neostencil.model.entities.Course;
import com.neostencil.projections.CourseProjection;
import com.neostencil.projections.TeacherProjection;
import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

public interface CourseRepository
    extends JpaRepository<Course, Integer>, JpaSpecificationExecutorWithProjection<Course>
// extends JpaRepository<Course,Integer>,JpaSpecificationExecutor<Course>
{

  Course findById(int id);

  Course findByCourseOldSlug(String slug);

  List<CourseProjection> findAllProjectedByPopularTrueAndStatus(PublishStatus publishStatus);

  Page<CourseProjection> findAllProjectedByPopularTrueAndStatus(PublishStatus publishStatus,
      Pageable pageable);

  Page<CourseProjection> findAllProjectedByPosition(Specification<Course> specification, Pageable pageable);

  // List<AllCustomCourse> findAllProjectedByPopularTrueAndStatusAndCourseExamSegment(PublishStatus
  // publishStatus,ExamSegmentTypes examSegmentTypes);

  List<CourseProjection> findAllProjectedByPopularTrueAndStatusAndCourseExamSegmentAndPositionIsGreaterThanAndPositionIsLessThanEqualOrderByPosition(
      PublishStatus status, ExamSegmentTypes examSegmentTypes, int greaterThanPosition,int lessThanEqualPosition);


  List<CourseProjection> findAllProjectedByStatus(PublishStatus status);

  List<AllCustomCourseWithBatch> findAllProjectedByCourseExamSegmentAndPriceAndStatus(
      ExamSegmentTypes examSegmentTypes, double price, PublishStatus publishStatus);

  List<CourseProjection> findAllProjectedBy();

  Page<CourseProjection> findAllProjectedBy(Pageable pageable);
  /*
   * @Query("Select cb.course from CourseBatch cb join fetch cb.course where cb.id=id") Course
   * findCourseByBatchId(int id);
   */

  @Query("select c from Course c where c.courseType='TEST_SERIES_ONLY' and c.status = 'publish' order by c.updatedAt desc")
  Page<Course> fetchTestSeriesCourse(Pageable pageable);

  @Query("select c from Course c where c.courseType='TEST_SERIES_ONLY' and c.status = 'publish' order by c.updatedAt desc")
  List<Course> fetchTestSeriesCourse();

  List<Course> findAllByOrderByUpdatedAtDesc();

  Page<Course> findAllByOrderByUpdatedAtDesc(Pageable pageable);

  Page<Course> findAllByOrderByUpdatedAt(Specification<Course> specification, Pageable pageable);

  @Query("select c.courseName,c.id from Course c")
  List<Course> fetchCourseNames();

  CourseProjection findAllProjectedById(int id);


}
