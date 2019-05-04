package com.neostencil.responses;

import java.util.Set;

/**
 * 
 * @author shilpa
 *
 */
public class TeacherSummaryResponse {

  private long noOfStudents;
  
  private long noOfCourses;
  
  private long noOfBatches;

  private Set<CustomCourse> courseBatches;
  
  private TeacherStudentResponse teacherStudentResponse;
  /**
   * @return the noOfStudents
   */
  public long getNoOfStudents() {
    return noOfStudents;
  }

  /**
   * @param noOfStudents the noOfStudents to set
   */
  public void setNoOfStudents(long noOfStudents) {
    this.noOfStudents = noOfStudents;
  }

  /**
   * @return the noOfCourses
   */
  public long getNoOfCourses() {
    return noOfCourses;
  }

  /**
   * @param noOfCourses the noOfCourses to set
   */
  public void setNoOfCourses(long noOfCourses) {
    this.noOfCourses = noOfCourses;
  }

  /**
   * @return the noOfBatches
   */
  public long getNoOfBatches() {
    return noOfBatches;
  }

  /**
   * @param noOfBatches the noOfBatches to set
   */
  public void setNoOfBatches(long noOfBatches) {
    this.noOfBatches = noOfBatches;
  }

  /**
   * @return the courseBatches
   */
  public Set<CustomCourse> getCourseBatches() {
    return courseBatches;
  }

  /**
   * @param courseBatches the courseBatches to set
   */
  public void setCourseBatches(Set<CustomCourse> courseBatches) {
    this.courseBatches = courseBatches;
  }

  /**
   * @return the teacherStudentResponse
   */
  public TeacherStudentResponse getTeacherStudentResponse() {
    return teacherStudentResponse;
  }

  /**
   * @param teacherStudentResponse the teacherStudentResponse to set
   */
  public void setTeacherStudentResponse(TeacherStudentResponse teacherStudentResponse) {
    this.teacherStudentResponse = teacherStudentResponse;
  }
  
  
  
}
