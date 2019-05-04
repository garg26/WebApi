package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neostencil.common.enums.PublishStatus;

@Entity
@Table(name = "ns_course_summary")
public class CourseSummary {

  @Id
  @Column(name = "course_summary_id", nullable = false)
  private int courseSummaryId;

  @Column(name = "course_slug")
  private String courseSlug;

  @Column(name = "course_title")
  private String courseTitle;

  @Column(name = "instructor_name")
  private String instructorName;

  @Column(name = "instructor_slug")
  private String instructorSlug;

  @Column(name = "institute_name")
  private String instituteName;

  @Column(name = "institute_slug")
  private String instituteSlug;

  @Column(name = "students_enrolled")
  private int studentsEnrolled;

  @Column(name = "course_image_url")
  private String courseImageUrl;

  @Column(name = "course_rating")
  private float courseRating = 0;

  @Column(name = "no_of_reviewers")
  private int noOfReviewers;

  @Column(name = "course_price")
  private double price;

  @Column(name = "start_date")
  private String startDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 2000)
  PublishStatus status;

  @JsonIgnore
  @JsonIgnoreProperties("relatedCourses")
  @ManyToMany(mappedBy = "relatedCourses", fetch = FetchType.LAZY)
  private Set<Course> parentCourses;

  @JsonIgnore
  @JsonIgnoreProperties("boughtTogetherCourses")
  @ManyToMany(mappedBy = "boughtTogetherCourses", fetch = FetchType.LAZY)
  private Set<Course> courses;


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
   * @return the parentCourses
   */
  public Set<Course> getParentCourses() {
    return parentCourses;
  }

  /**
   * @param parentCourses the parentCourses to set
   */
  public void setParentCourses(Set<Course> parentCourses) {
    this.parentCourses = parentCourses;
  }

  /**
   * @return the courses
   */
  public Set<Course> getCourses() {
    return courses;
  }

  /**
   * @param courses the courses to set
   */
  public void setCourses(Set<Course> courses) {
    this.courses = courses;
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


  public String getInstructorSlug() {
    return instructorSlug;
  }


  public void setInstructorSlug(String instructorSlug) {
    this.instructorSlug = instructorSlug;
  }

  public String getInstituteSlug() {
    return instituteSlug;
  }

  public void setInstituteSlug(String instituteSlug) {
    this.instituteSlug = instituteSlug;
  }

  public String getCourseImageUrl() {
    return courseImageUrl;
  }

  public void setCourseImageUrl(String courseImageUrl) {
    this.courseImageUrl = courseImageUrl;
  }

  public float getCourseRating() {
    return courseRating;
  }

  public void setCourseRating(float courseRating) {
    this.courseRating = courseRating;
  }

  public int getNoOfReviewers() {
    return noOfReviewers;
  }

  public void setNoOfReviewers(int noOfReviewers) {
    this.noOfReviewers = noOfReviewers;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getStartDate() {
    return startDate;
  }

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
}
