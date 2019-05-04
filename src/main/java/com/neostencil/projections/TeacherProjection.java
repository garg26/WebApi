package com.neostencil.projections;

import org.springframework.beans.factory.annotation.Value;
import com.neostencil.common.enums.PublishStatus;


public interface TeacherProjection {

  int getId();

  String getTeacherName();

  String getSlug();

  String getSubjects();

  String getDisplayPictureUrl();

  PublishStatus getStatus();

  @Value("#{target.institute.name}")
  String getName();

  @Value("#{target.institute.instituteSlug}")
  String getInstituteSlug();


  @Value("#{target.courses.size()}")
  int getCourseSize();

  int getNoOfReviewers();

  int getNoOfStudents();

  float getTeacherRating();

  int getPosition();

  String getImageAltText();

}
