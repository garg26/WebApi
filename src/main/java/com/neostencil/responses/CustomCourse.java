package com.neostencil.responses;

import com.neostencil.common.enums.UserCourseLinkageStatus;
import java.util.Date;

public class CustomCourse {

  /*
   * @Id
   * 
   * @Column(name="course_slug")
   */
  String courseSlug;

  // @Column()
  String courseTitle;
  String instructorName;
  String instituteName;
  int studentsEnrolled;
  String courseOldSlug;
  String courseImage;
  int courseBatchId;
  UserCourseLinkageStatus status;
  String startDate;
  int remainingValidity;

  /**
   * @return the courseTitle
   */
  public String getCourseTitle() {
    return courseTitle;
  }

  /**
   * @param courseTitle the courseTitle to set
   */
  public void setCourseTitle(String courseTitle) {
    this.courseTitle = courseTitle;
  }

  /**
   * @return the instructorName
   */
  public String getInstructorName() {
    return instructorName;
  }

  /**
   * @param instructorName the instructorName to set
   */
  public void setInstructorName(String instructorName) {
    this.instructorName = instructorName;
  }

  /**
   * @return the instituteName
   */
  public String getInstituteName() {
    return instituteName;
  }

  /**
   * @param instituteName the instituteName to set
   */
  public void setInstituteName(String instituteName) {
    this.instituteName = instituteName;
  }

  /**
   * @return the studentsEnrolled
   */
  public int getStudentsEnrolled() {
    return studentsEnrolled;
  }

  /**
   * @param studentsEnrolled the studentsEnrolled to set
   */
  public void setStudentsEnrolled(int studentsEnrolled) {
    this.studentsEnrolled = studentsEnrolled;
  }

  /**
   * @return the courseOldSlug
   */
  public String getCourseOldSlug() {
    return courseOldSlug;
  }

  /**
   * @param courseOldSlug the courseOldSlug to set
   */
  public void setCourseOldSlug(String courseOldSlug) {
    this.courseOldSlug = courseOldSlug;
  }

  /**
   * @return the courseImage
   */
  public String getCourseImage() {
    return courseImage;
  }

  /**
   * @param courseImage the courseImage to set
   */
  public void setCourseImage(String courseImage) {
    this.courseImage = courseImage;
  }

  /**
   * @return the courseBatchId
   */
  public int getCourseBatchId() {
    return courseBatchId;
  }

  /**
   * @param courseBatchId the courseBatchId to set
   */
  public void setCourseBatchId(int courseBatchId) {
    this.courseBatchId = courseBatchId;
  }

  /**
   * @return the courseSlug
   */
  public String getCourseSlug() {
    return courseSlug;
  }

  /**
   * @param courseSlug the courseSlug to set
   */
  public void setCourseSlug(String courseSlug) {
    this.courseSlug = courseSlug;
  }

  /**
   * @return the status
   */
  public UserCourseLinkageStatus getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(UserCourseLinkageStatus status) {
    this.status = status;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  /**
   * @return the remainingValidity
   */
  public int getRemainingValidity() {
    return remainingValidity;
  }

  /**
   * @param remainingValidity the remainingValidity to set
   */
  public void setRemainingValidity(int remainingValidity) {
    this.remainingValidity = remainingValidity;
  }
}
