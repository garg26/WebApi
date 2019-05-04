package com.neostencil.requests;

public class ParentUnitsRequest {

  private int batchId;

  private long studentId;

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
   * @return the studentId
   */
  public long getStudentId() {
    return studentId;
  }

  /**
   * @param studentId the studentId to set
   */
  public void setStudentId(long studentId) {
    this.studentId = studentId;
  }

}
