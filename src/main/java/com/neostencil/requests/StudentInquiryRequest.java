package com.neostencil.requests;

/**
 * Request object for creating a query object. This will be used to send student's queries to
 * teachers of a particular course.
 * 
 * @author shilpa
 *
 */
public class StudentInquiryRequest {

  private String message;

  private String courseSlug;

  private int unitId;


  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
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
   * @return the unitId
   */
  public int getUnitId() {
    return unitId;
  }

  /**
   * @param unitId the unitId to set
   */
  public void setUnitId(int unitId) {
    this.unitId = unitId;
  }



}
