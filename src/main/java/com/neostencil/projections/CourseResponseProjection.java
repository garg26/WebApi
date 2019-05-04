package com.neostencil.projections;

import com.neostencil.common.enums.PublishStatus;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;

public interface CourseResponseProjection {

  int getId();
  String getCourseTitle();
  String getCourseName();
  String getCourseDescription();
  int getStudentsEnrolled();
  String getCourseSubject();
  float getCourseRating();
  int getNoOfReviewers();
  String getCourseOldSlug();
  PublishStatus getStatus();
  double getPrice();
  String getStartDate();
  String getCourseImageUrl();
  @Value("#{target.institute.name}")
  String getInstituteName();
  @Value("#{target.institute.instituteSlug}")
  String getInstituteSlug();
  String getInstructorName();
  String getInstructorEmailId();
  String getInstructorSlug();
  String getImageAltText();



}
