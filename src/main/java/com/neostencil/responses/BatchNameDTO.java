package com.neostencil.responses;

public class BatchNameDTO {

  int batchId;
  String batchName;
  String courseName;
  int courseId;

  /**
   * @return the batchId
   */
  public int getBatchId() {
    return batchId;
  }

  /**
   * @param batchId the batchId to set
   */
  public void setBatchId(int batchId) {
    this.batchId = batchId;
  }

  /**
   * @return the batchName
   */
  public String getBatchName() {
    return batchName;
  }

  /**
   * @param batchName the batchName to set
   */
  public void setBatchName(String batchName) {
    this.batchName = batchName;
  }

  /**
   * @return the courseName
   */
  public String getCourseName() {
    return courseName;
  }

  /**
   * @param courseName the courseName to set
   */
  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  /**
   * @return the courseId
   */
  public int getCourseId() {
    return courseId;
  }

  /**
   * @param courseId the courseId to set
   */
  public void setCourseId(int courseId) {
    this.courseId = courseId;
  }
}
