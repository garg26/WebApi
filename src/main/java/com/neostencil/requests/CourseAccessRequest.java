package com.neostencil.requests;

public class CourseAccessRequest {

  String userEmailId;
  int batchId;

  /**
   * @return the userEmailId
   */
  public String getUserEmailId() {
    return userEmailId;
  }

  /**
   * @param userEmailId the userEmailId to set
   */
  public void setUserEmailId(String userEmailId) {
    this.userEmailId = userEmailId;
  }

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

}
