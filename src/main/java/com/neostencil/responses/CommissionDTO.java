package com.neostencil.responses;

public class CommissionDTO{

  private int batchId;

  /**
   * This will be the courseName + batchName(CourseName|BatchName)
   */
  private String name;
  private double commission;

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
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the commission
   */
  public double getCommission() {
    return commission;
  }

  /**
   * @param commission the commission to set
   */
  public void setCommission(double commission) {
    this.commission = commission;
  }



}
