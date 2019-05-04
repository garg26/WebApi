package com.neostencil.model.entities;

import com.neostencil.common.enums.CourseType;
import com.neostencil.common.enums.PublishStatus;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;

public interface AllCustomCourseWithBatch {

  int getId();
  String getCourseTitle();
  String getCourseName();
  String getCourseOldSlug();
  String getCourseImageUrl();
  float getCourseRating();
  String getStartDate();
  String getCourseSubject();
  CourseType getCourseType();
  PublishStatus getStatus();

  @Value("#{target.institute.name}")
  String getInstituteName();
  @Value("#{target.institute.instituteSlug}")
  String getInstituteSlug();

  @Value("#{target.instructors[0].teacherName}")
  String getTeacherName();

  @Value("#{target.instructors[0].slug}")
  String getSlug();

  double getPrice();
  int getStudentsEnrolled();
  int getDiscount();
  Set<CourseBatch> getBatches();
}
