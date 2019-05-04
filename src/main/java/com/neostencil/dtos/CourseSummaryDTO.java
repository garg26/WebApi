package com.neostencil.dtos;

import java.util.LinkedList;
import java.util.List;
import com.neostencil.common.enums.PublishStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CourseSummaryDTO {

  private int courseSummaryId;

  private String courseOldSlug;

  private String courseTitle;

  private String instructorName;

  private String instructorSlug;

  private String instituteName;

  private String instituteSlug;

  private int studentsEnrolled;

  private String courseImageUrl;

  private float courseRating = 0;

  private int noOfReviewers;

  private double price;

  private String startDate;


  PublishStatus status;

  List<String> teachers = new LinkedList<String>();
  Map<String, String> teachersMap = new TreeMap<String, String>();

  public Map<String, String> getTeachersMap() {
    return teachersMap;
  }

  public void setTeachersMap(Map<String, String> teachersMap) {
    this.teachersMap = teachersMap;
  }

  /**
   * @return the courseSummaryId
   */
  public int getCourseSummaryId() {
    return courseSummaryId;
  }

  /**
   * @param courseSummaryId the courseSummaryId to set
   */
  public void setCourseSummaryId(int courseSummaryId) {
    this.courseSummaryId = courseSummaryId;
  }

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
   * @return the instructorSlug
   */
  public String getInstructorSlug() {
    return instructorSlug;
  }

  /**
   * @param instructorSlug the instructorSlug to set
   */
  public void setInstructorSlug(String instructorSlug) {
    this.instructorSlug = instructorSlug;
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
   * @return the instituteSlug
   */
  public String getInstituteSlug() {
    return instituteSlug;
  }

  /**
   * @param instituteSlug the instituteSlug to set
   */
  public void setInstituteSlug(String instituteSlug) {
    this.instituteSlug = instituteSlug;
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
   * @return the courseImageUrl
   */
  public String getCourseImageUrl() {
    return courseImageUrl;
  }

  /**
   * @param courseImageUrl the courseImageUrl to set
   */
  public void setCourseImageUrl(String courseImageUrl) {
    this.courseImageUrl = courseImageUrl;
  }

  /**
   * @return the courseRating
   */
  public float getCourseRating() {
    return courseRating;
  }

  /**
   * @param courseRating the courseRating to set
   */
  public void setCourseRating(float courseRating) {
    this.courseRating = courseRating;
  }

  /**
   * @return the noOfReviewers
   */
  public int getNoOfReviewers() {
    return noOfReviewers;
  }

  /**
   * @param noOfReviewers the noOfReviewers to set
   */
  public void setNoOfReviewers(int noOfReviewers) {
    this.noOfReviewers = noOfReviewers;
  }

  /**
   * @return the price
   */
  public double getPrice() {
    return price;
  }

  /**
   * @param price the price to set
   */
  public void setPrice(double price) {
    this.price = price;
  }

  /**
   * @return the startDate
   */
  public String getStartDate() {
    return startDate;
  }

  /**
   * @param startDate the startDate to set
   */
  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  /**
   * @return the status
   */
  public PublishStatus getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(PublishStatus status) {
    this.status = status;
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
   * @return the teachers
   */
  public List<String> getTeachers() {
    return teachers;
  }

  /**
   * @param teachers the teachers to set
   */
  public void setTeachers(List<String> teachers) {
    this.teachers = teachers;
  }


}
