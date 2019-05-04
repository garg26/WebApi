package com.neostencil.projections;

import java.util.List;
import java.util.Set;
import org.springframework.data.rest.core.config.Projection;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.model.entities.Achievements;
import com.neostencil.model.entities.BreadCrumb;
import com.neostencil.model.entities.FeaturedImage;
import com.neostencil.model.entities.Highlights;
import com.neostencil.model.entities.MetaInformation;
import com.neostencil.model.entities.TeacherDetails;

@Projection(name = "teacherDetailedProjection", types = TeacherDetails.class)
public interface TeacherDetailedProjection {

  int getId();

  String getTeacherName();

  String getSlug();

  String getSubjects();

  String getDisplayPictureUrl();

  PublishStatus getStatus();

  InstituteProjection getInstitute();

  Set<CourseProjection> getCourses();

  String getTeacherBio();

  int getNoOfReviewers();

  int getNoOfStudents();

  float getTeacherRating();

  int getPosition();

  String getImageAltText();

  String getTeacherDescription();

  List<String> getTeacherGallery();

  int getTotalExperience();

  String getTeacherLocation();

  List<Achievements> getHighlight();

  List<Highlights> getEducation();

  List<Highlights> getExperience();

  String getAddress();

  String getContactNo();

  String getEmailId();

  String getWebsite();

  String getFacebookUrl();

  String getTwitterUrl();

  String getLinkedinUrl();

  String getGooglePlusUrl();

  List<String> getCertificates();

  List<MetaInformation> getMetaList();

  FeaturedImage getFeaturedImage();

  String getTeacherExamSegment();

  String getTeacherCategory();

  BreadCrumb getBreadCrumb();

  String getTitleTag();

}
