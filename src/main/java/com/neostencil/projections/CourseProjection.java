package com.neostencil.projections;

import java.util.Date;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import com.neostencil.common.enums.CourseType;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.model.entities.Course;

@Projection(name = "courseProjection", types = Course.class)
public interface CourseProjection {

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

  int getPosition();

  String getImageAltText();

  @Value("#{target.institute.name}")
  String getName();

  @Value("#{target.institute.instituteSlug}")
  String getInstituteSlug();

  Set<TeacherProjection> getInstructors();

  double getPrice();

  int getStudentsEnrolled();

  int getDiscount();

  int getNoOfReviewers();

  boolean isPopular();

  Date getUpdatedAt();

  Date getCreatedAt();

  String getInstructorName();
}
