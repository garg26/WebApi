package com.neostencil.dtos;

import java.util.Set;

/**
 * For summarized teacher information
 * 
 * @author shilpa
 *
 */
public class TeacherSummaryDTO {

  private int id;
  private String teacherName;
  private String subjects;
  private String slug;
  private String displayPictureUrl;
  private String imageAltText;
  private int noOfReviewers;
  private int noOfStudents;
  private String emailId;
  private InstituteDTO institute;
  private Set<CourseSummaryDTO> courses;


  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return the teacherName
   */
  public String getTeacherName() {
    return teacherName;
  }

  /**
   * @param teacherName the teacherName to set
   */
  public void setTeacherName(String teacherName) {
    this.teacherName = teacherName;
  }

  /**
   * @return the subjects
   */
  public String getSubjects() {
    return subjects;
  }

  /**
   * @param subjects the subjects to set
   */
  public void setSubjects(String subjects) {
    this.subjects = subjects;
  }

  /**
   * @return the slug
   */
  public String getSlug() {
    return slug;
  }

  /**
   * @param slug the slug to set
   */
  public void setSlug(String slug) {
    this.slug = slug;
  }

  /**
   * @return the displayPictureUrl
   */
  public String getDisplayPictureUrl() {
    return displayPictureUrl;
  }

  /**
   * @param displayPictureUrl the displayPictureUrl to set
   */
  public void setDisplayPictureUrl(String displayPictureUrl) {
    this.displayPictureUrl = displayPictureUrl;
  }

  /**
   * @return the imageAltText
   */
  public String getImageAltText() {
    return imageAltText;
  }

  /**
   * @param imageAltText the imageAltText to set
   */
  public void setImageAltText(String imageAltText) {
    this.imageAltText = imageAltText;
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
   * @return the noOfStudents
   */
  public int getNoOfStudents() {
    return noOfStudents;
  }

  /**
   * @param noOfStudents the noOfStudents to set
   */
  public void setNoOfStudents(int noOfStudents) {
    this.noOfStudents = noOfStudents;
  }

  /**
   * @return the emailId
   */
  public String getEmailId() {
    return emailId;
  }

  /**
   * @param emailId the emailId to set
   */
  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  /**
   * @return the institute
   */
  public InstituteDTO getInstitute() {
    return institute;
  }

  /**
   * @param institute the institute to set
   */
  public void setInstitute(InstituteDTO institute) {
    this.institute = institute;
  }


  public Set<CourseSummaryDTO> getCourses() {
    return courses;
  }

  public void setCourses(Set<CourseSummaryDTO> courses) {
    this.courses = courses;
  }
}
